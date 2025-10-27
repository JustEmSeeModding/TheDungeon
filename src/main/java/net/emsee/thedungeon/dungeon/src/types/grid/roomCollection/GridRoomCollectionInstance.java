package net.emsee.thedungeon.dungeon.src.types.grid.roomCollection;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.utils.WeightedMap;

import java.util.*;

public class GridRoomCollectionInstance {
    private final Map<AbstractGridRoom, RequiredRoomPlacementsInstance> requiredPlacements = new LinkedHashMap<>();
    private final Map<List<AbstractGridRoom>, RequiredRoomPlacementsInstance> requiredListPlacements = new LinkedHashMap<>();

    final GridRoomCollection collection;
    public GridRoomCollectionInstance(GridRoomCollection collection) {
        this.collection = collection;

        // copy the requirements into the instance list
        collection.getRequiredPlacements().forEach((room, placements) -> {
            requiredPlacements.put(room, new RequiredRoomPlacementsInstance(placements.min, placements.max));
        });
        collection.getRequiredListPlacements().forEach((list, placements) -> {
            requiredListPlacements.put(list, new RequiredRoomPlacementsInstance(placements.min, placements.max));
        });
    }

    public AbstractGridRoom getRandomRoom(Random random) {
        return getAllPossibleRooms().getRandom(random);
    }

    public AbstractGridRoom getRandomRoom(int maxRoomScale, Random random) {
        WeightedMap.Int<AbstractGridRoom> returnMap = new WeightedMap.Int<>();
        for (AbstractGridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                returnMap.put(gridRoom, gridRoom.getWeight());
            }
        }
        return returnMap.getRandom(random);
    }

    public AbstractGridRoom getRandomRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, Random random) {
        WeightedMap.Int<AbstractGridRoom> returnList = new WeightedMap.Int<>();
        for (AbstractGridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                returnList.put(gridRoom, gridRoom.getWeight());
            }
        }
        if (!returnList.isEmpty()) {
            return returnList.getRandom(random);
        }
        return null;
    }

    public AbstractGridRoom getRandomRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, int maxRoomScale, Random random) {
        WeightedMap.Int<AbstractGridRoom> returnList = new WeightedMap.Int<>();

        for (AbstractGridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                    returnList.put(gridRoom, gridRoom.getWeight());
                }
            }
        }
        if (!returnList.isEmpty()) {
            return returnList.getRandom(random);
        }
        return null;
    }

    /**
     * Returns a map of all rooms that are eligible for placement based on individual and grouped placement constraints.
     */
    private WeightedMap.Int<AbstractGridRoom> getAllPossibleRooms() {
        WeightedMap.Int<AbstractGridRoom> toReturn = new WeightedMap.Int<>();
        for (AbstractGridRoom room : collection.getAllRooms().keySet()) {
            boolean allowed = true;
            if (requiredPlacements.containsKey(room)) {
                RequiredRoomPlacementsInstance constraints = requiredPlacements.get(room);
                // Skip if no maximum
                if (constraints.hasMax() && !constraints.placementBelowMax()) {
                    allowed = false;
                }
            }
            if (allowed)
                for (List<AbstractGridRoom> rooms : requiredListPlacements.keySet()) {
                    if (rooms.contains(room)) {
                        RequiredRoomPlacementsInstance constraints = requiredListPlacements.get(rooms);
                        // Skip if no maximum
                        if (constraints.hasMax() && !constraints.placementBelowMax()) {
                            allowed = false;
                            break;
                        }
                    }
                }
            if (allowed) {
                toReturn.put(room, room.getWeight());
            }
        }
        return toReturn;
    }

    /****
     * Updates the placement count for required rooms or required room groups when a room is placed.
     * <p>
     * in the case that the room is both separate or in a list, or mentioned in multiple list:
     * the separate one will be marked first, then the first list found
     */
    public void updatePlacedRequirements(AbstractGridRoom placed) {
        if (requiredPlacements.containsKey(placed)) {
            requiredPlacements.get(placed).addPlacement();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "requirement found:{}",placed);
            if (requiredPlacements.get(placed).max>=0)
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}-{}",requiredPlacements.get(placed).placed, requiredPlacements.get(placed).min, requiredPlacements.get(placed).max);
            else
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}",requiredPlacements.get(placed).placed, requiredPlacements.get(placed).min);
            return;
        }
        for (List<AbstractGridRoom> rooms : requiredListPlacements.keySet()) {
            if (rooms.contains(placed)) {
                requiredListPlacements.get(rooms).addPlacement();
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "requirement found in list:{}",placed);
                if (requiredListPlacements.get(rooms).max>=0)
                    DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}-{}",requiredListPlacements.get(rooms).placed,requiredListPlacements.get(rooms).min,requiredListPlacements.get(rooms).max);
                else
                    DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}",requiredListPlacements.get(rooms).placed,requiredListPlacements.get(rooms).min);
                return;
            }
        }
    }

    /**
     * Checks whether all required room and room group placement constraints have been satisfied.
     *
     * @return true if all individual and grouped required rooms have met their minimum placement counts; false otherwise
     */
    public boolean requiredRoomsDone() {
        for (AbstractGridRoom room : requiredPlacements.keySet()) {
            if (!requiredPlacements.get(room).placementAboveMin())
                return false;
        }
        for (List<AbstractGridRoom> rooms : requiredListPlacements.keySet()) {
            if (!requiredListPlacements.get(rooms).placementAboveMin())
                return false;
        }
        return true;
    }

    public GridRoomCollection getRaw() {
        return collection;
    }

    private static class RequiredRoomPlacementsInstance extends GridRoomCollection.RequiredRoomPlacements {
        int placed = 0;

        RequiredRoomPlacementsInstance(int min, int max) {
            super(min, max);
        }

        boolean hasMax() {
            return max > 0;
        }

        boolean placementBelowMax() {
            return placed < max;
        }

        public void addPlacement() {
            placed++;
        }

        boolean placementAboveMin() {
            return placed >= min;
        }
    }
}
