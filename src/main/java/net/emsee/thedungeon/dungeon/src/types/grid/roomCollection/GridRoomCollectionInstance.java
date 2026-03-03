package net.emsee.thedungeon.dungeon.src.types.grid.roomCollection;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.types.grid.GridDungeon;
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

    public AbstractGridRoom getRandomRoom(Random random, AbstractGridRoom.PredicateData placementData) {
        return getAllPossibleRooms(placementData).getRandom(random);
    }

    public AbstractGridRoom getRandomRoom(int maxRoomScale, Random random, AbstractGridRoom.PredicateData placementData) {
        WeightedMap.Int<AbstractGridRoom> returnMap = new WeightedMap.Int<>();
        for (AbstractGridRoom gridRoom : getAllPossibleRooms(placementData).keySet()) {
            if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                returnMap.put(gridRoom, gridRoom.getWeight());
            }
        }
        return returnMap.getRandom(random);
    }

    public AbstractGridRoom getRandomRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, Random random, AbstractGridRoom.PredicateData placementData) {
        WeightedMap.Int<AbstractGridRoom> returnList = new WeightedMap.Int<>();
        for (AbstractGridRoom gridRoom : getAllPossibleRooms(placementData).keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                returnList.put(gridRoom, gridRoom.getWeight());
            }
        }
        if (!returnList.isEmpty()) {
            return returnList.getRandom(random);
        }
        return null;
    }

    public AbstractGridRoom getRandomRequiredRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, Random random, AbstractGridRoom.PredicateData placementData) {
        WeightedMap.Int<AbstractGridRoom> returnList = new WeightedMap.Int<>();
        for (AbstractGridRoom gridRoom : getAllPossibleRequiredRooms(placementData).keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                returnList.put(gridRoom, gridRoom.getWeight());
            }
        }
        if (!returnList.isEmpty()) {
            return returnList.getRandom(random);
        }
        return null;
    }

    public AbstractGridRoom getRandomRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, int maxRoomScale, Random random, AbstractGridRoom.PredicateData placementData) {
        WeightedMap.Int<AbstractGridRoom> returnList = new WeightedMap.Int<>();

        for (AbstractGridRoom gridRoom : getAllPossibleRooms(placementData).keySet()) {
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
    private WeightedMap.Int<AbstractGridRoom> getAllPossibleRooms(AbstractGridRoom.PredicateData placementData) {
        WeightedMap.Int<AbstractGridRoom> toReturn = new WeightedMap.Int<>();
        for (AbstractGridRoom room : collection.getAllRooms().keySet()) {
            boolean allowed = room.canPlace(placementData);
            if (!allowed) continue;
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

    /**
     * Returns a map of all required rooms that are eligible for placement based on individual and grouped placement constraints.
     */
    private WeightedMap.Int<AbstractGridRoom> getAllPossibleRequiredRooms(AbstractGridRoom.PredicateData placementData) {
        WeightedMap.Int<AbstractGridRoom> toReturn = new WeightedMap.Int<>();
        for (AbstractGridRoom room : collection.getAllRooms().keySet()) {
            boolean allowed = room.canPlace(placementData);
            if (!allowed) continue;
            if (requiredPlacements.containsKey(room)) {
                RequiredRoomPlacementsInstance constraints = requiredPlacements.get(room);
                // test for max
                if (constraints.hasMax() && !constraints.placementBelowMax()) {
                    allowed = false;
                }
                if (!constraints.hasMin() || constraints.placementAboveMin()) {
                    allowed = false;
                }
            } else {
                allowed = false;
            }
            if (allowed)
                for (List<AbstractGridRoom> rooms : requiredListPlacements.keySet()) {
                    if (rooms.contains(room)) {
                        RequiredRoomPlacementsInstance constraints = requiredListPlacements.get(rooms);
                        // Skip if no maximum
                        if ((constraints.hasMax() && !constraints.placementBelowMax()) || !constraints.hasMin() || constraints.placementAboveMin()) {
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

    /**
     *
     *  Updates the placement count for required rooms or required room groups when a room is placed.
     *  <p>
     *  in the case that the room is both separate or in a list, or mentioned in multiple list:
     *  the separate one will be marked first, then the first list found

     * @return true if a required room was placed
     */
    public boolean updatePlacedRequirements(AbstractGridRoom placed) {
        if (requiredPlacements.containsKey(placed)) {
            requiredPlacements.get(placed).addPlacement();
            DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "requirement found:{}",placed);
            if (requiredPlacements.get(placed).max>=0)
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}-{}",requiredPlacements.get(placed).placed, requiredPlacements.get(placed).min, requiredPlacements.get(placed).max);
            else
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}",requiredPlacements.get(placed).placed, requiredPlacements.get(placed).min);
            return requiredPlacements.get(placed).placed<=requiredPlacements.get(placed).max;
        }
        for (List<AbstractGridRoom> rooms : requiredListPlacements.keySet()) {
            if (rooms.contains(placed)) {
                requiredListPlacements.get(rooms).addPlacement();
                DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "requirement found in list:{}",placed);
                if (requiredListPlacements.get(rooms).max>=0)
                    DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}-{}",requiredListPlacements.get(rooms).placed,requiredListPlacements.get(rooms).min,requiredListPlacements.get(rooms).max);
                else
                    DebugLog.logInfo(DebugLog.DebugType.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}",requiredListPlacements.get(rooms).placed,requiredListPlacements.get(rooms).min);
                return requiredListPlacements.get(rooms).placed<=requiredListPlacements.get(rooms).max;
            }
        }
        return false;
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

        boolean hasMin() {
            return min > 0;
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
