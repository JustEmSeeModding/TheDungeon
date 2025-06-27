package net.emsee.thedungeon.dungeon.roomCollections;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.util.Connection;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;

public abstract class GridRoomCollection {
    public final GridRoomCollection instance;

    private final WeightedMap.Int<GridRoom> allGridRooms = new WeightedMap.Int<>();
    private final List<ConnectionRule> connectionRules = new ArrayList<>();
    private final List<FailRule> failRules = new ArrayList<>();
    private final Map<GridRoom, RequiredRoomPlacements> requiredPlacements = new HashMap<>();
    private final Map<List<GridRoom>, RequiredRoomPlacements> requiredListPlacements = new HashMap<>();
    private GridRoom fallbackGridRoom = null;
    private GridRoom startingRoom = null;
    private final int gridCellWidth;
    private final int gridCellHeight;
    private final StructureProcessorList structureProcessors = new StructureProcessorList(new ArrayList<>());

    // constructor

    protected GridRoomCollection(int gridCellWidth, int gridCellHeight) {
        this.gridCellWidth = gridCellWidth;
        this.gridCellHeight = gridCellHeight;
        this.instance = this;
    }

    /****
     * Adds a room to the collection if its dimensions match the collection's required width and height.
     *
     * @param gridRoom the room to add
     * @return this collection instance for method chaining
     */


    public GridRoomCollection addRoom(GridRoom gridRoom) {
        if (gridRoom.getGridCellWidth() != gridCellWidth || gridRoom.getGridCellHeight() != gridCellHeight)
            throw new IllegalStateException("Room " + gridRoom + " is not the same size as the RoomCollection " + this);
        int weight = gridRoom.getWeight();
        allGridRooms.put(gridRoom, weight);

        return this;
    }

    /**
     * Adds multiple rooms to the collection, validating each for dimension compatibility.
     *
     * @param rooms the list of rooms to add
     * @return this collection instance for method chaining
     */
    public GridRoomCollection addRooms(List<GridRoom> rooms) {
        for (GridRoom room : rooms) {
            addRoom(room);
        }
        return this;
    }

    /**
     * Sets the starting room for the collection if its dimensions match the collection's required width and height.
     *
     * @param gridRoom the room to set as the starting room; ignored if null or if its dimensions do not match
     * @return this collection instance for chaining
     */
    public GridRoomCollection setStartingRoom(GridRoom gridRoom) {
        if (gridRoom == null) return this;
        if (gridRoom.getGridCellWidth() != gridCellWidth || gridRoom.getGridCellHeight() != gridCellHeight)
            throw new IllegalStateException("Room " + gridRoom + " is not the same size as the RoomCollection " + this);
        startingRoom = (gridRoom);
        return this;
    }

    /****
     * Adds a room to the collection with specified minimum and maximum placement requirements.
     * <p>
     * The room will be considered required and must be placed at least `requiredAmount` times and at most `maxAmount` times during dungeon generation. The room must match the collection's dimensions; otherwise, it is not added.
     */
    public GridRoomCollection addRequiredRoom(int requiredAmount, int maxAmount, GridRoom room) {
        if (room.getGridCellWidth() != gridCellWidth || room.getGridCellHeight() != gridCellHeight)
            throw new IllegalStateException("Room " + room + " is not the same size as the RoomCollection " + this);

        addRoom(room);
        requiredPlacements.put(room, new RequiredRoomPlacements(requiredAmount, maxAmount));

        return this;
    }


    /**
     * Adds a required room with a minimum placement count and no maximum constraint.
     */
    public GridRoomCollection addRequiredRoom(int requiredAmount, GridRoom room) {
        return addRequiredRoom(requiredAmount, -1, room);
    }

    /**
     * Adds a group of rooms with shared placement constraints, where placing any one room counts toward the group's required and maximum placement totals.
     */
    public GridRoomCollection addRequiredRoomsOf(int requiredAmount, int maxAmount, List<GridRoom> rooms) {
        requiredListPlacements.put(rooms, new RequiredRoomPlacements(requiredAmount, maxAmount));
        for (GridRoom room : rooms)
            addRoom(room);
        return this;
    }

    /**
     * Adds a group of rooms with a shared minimum required placement count.
     * When any room in the list is placed, it counts toward fulfilling the group's requirement.
     */
    public GridRoomCollection addRequiredRoomsOf(int requiredAmount, List<GridRoom> rooms) {
        return addRequiredRoomsOf(requiredAmount, -1, rooms);
    }

    /**
     * Sets the fallback room to be used when no other room fits.
     */
    public GridRoomCollection setFallback(GridRoom fallbackGridRoom) {
        if (fallbackGridRoom.getGridCellWidth() != gridCellWidth || fallbackGridRoom.getGridCellHeight() != gridCellHeight)
            throw new IllegalStateException("Room " + fallbackGridRoom + " is not the same size as the RoomCollection " + this);
        if (fallbackGridRoom.getHeightScale() > 1 || fallbackGridRoom.getRotatedEastSizeScale(Rotation.NONE) > 1 || fallbackGridRoom.getRotatedNorthSizeScale(Rotation.NONE) > 1)
            throw new IllegalStateException("Fallback can not be larger than scale 1,1");
        this.fallbackGridRoom = fallbackGridRoom;
        return this;
    }

    /**
     * Adds a connection rule to the collection.
     */
    public GridRoomCollection addTagRule(ConnectionRule rule) {
        connectionRules.add(rule);
        return this;
    }

    public GridRoomCollection addTagRule(FailRule rule) {
        failRules.add(rule);
        return this;
    }

    public GridRoomCollection withStructureProcessor(StructureProcessor processor) {
        structureProcessors.list().add(processor);
        return this;
    }

    // methods

    //public List<GridRoom> GetRequiredRooms() {return List.copyOf(requiredRooms);}
    public List<ConnectionRule> getConnectionRules() {
        return List.copyOf(connectionRules);
    }

    public List<FailRule> getFailRules() {
        return List.copyOf(failRules);
    }

    public GridRoom getFallback() {
        return fallbackGridRoom;
    }

    public int getGridCellWidth() {
        return gridCellWidth;
    }

    public int getGridCellHeight() {
        return gridCellHeight;
    }

    public GridRoom getRandomRoom(Random random) {
        GridRoom toReturn = Objects.requireNonNull(getAllPossibleRooms().getRandom(random));
        return toReturn.getCopy();
    }

    public GridRoom getRandomRoom(int maxRoomScale, Random random) {
        WeightedMap.Int<GridRoom> returnMap = new WeightedMap.Int<>();
        for (GridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                returnMap.put(gridRoom, gridRoom.getWeight());
            }
        }
        GridRoom toReturn = Objects.requireNonNull(returnMap.getRandom(random));
        return toReturn.getCopy();
    }

    public GridRoom getRandomRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, Random random) {
        WeightedMap.Int<GridRoom> returnList = new WeightedMap.Int<>();
        for (GridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                returnList.put(gridRoom, gridRoom.getWeight());
            }
        }
        if (!returnList.isEmpty()) {
            GridRoom toReturn = Objects.requireNonNull(returnList.getRandom(random));
            return toReturn.getCopy();
        }
        return null;
    }

    public GridRoom getRandomRoomByConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules, int maxRoomScale, Random random) {
        WeightedMap.Int<GridRoom> returnList = new WeightedMap.Int<>();

        for (GridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                    returnList.put(gridRoom, gridRoom.getWeight());
                }
            }
        }
        if (!returnList.isEmpty()) {
            GridRoom toReturn = Objects.requireNonNull(returnList.getRandom(random));
            return toReturn.getCopy();
        }
        return null;
    }

    /**
     * Returns a map of all rooms that are eligible for placement based on individual and grouped placement constraints.
     */
    private WeightedMap.Int<GridRoom> getAllPossibleRooms() {
        WeightedMap.Int<GridRoom> toReturn = new WeightedMap.Int<>();
        for (GridRoom room : allGridRooms.keySet()) {
            boolean allowed = true;
            if (requiredPlacements.containsKey(room)) {
                RequiredRoomPlacements constraints = requiredPlacements.get(room);
                // Skip if no maximum
                if (constraints.hasMax() && !constraints.placementBelowMax()) {
                    allowed = false;
                }
            }
            if (allowed)
                for (List<GridRoom> rooms : requiredListPlacements.keySet()) {
                    if (rooms.contains(room)) {
                        RequiredRoomPlacements constraints = requiredListPlacements.get(rooms);
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

    public abstract GridRoomCollection getCopy();

    /****
     * Updates the placement count for required rooms or required room groups when a room is placed.
     */
    public void updatePlacedRequirements(GridRoom placed) {
        if (requiredPlacements.containsKey(placed)) {
            requiredPlacements.get(placed).addPlacement();
            DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_REQUIRED_PLACEMENTS, "requirement found:{}",placed);
            if (requiredPlacements.get(placed).max>=0)
                DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}-{}",requiredPlacements.get(placed).placed, requiredPlacements.get(placed).min, requiredPlacements.get(placed).max);
            else
                DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}",requiredPlacements.get(placed).placed, requiredPlacements.get(placed).min);
            return;
        }
        for (List<GridRoom> rooms : requiredListPlacements.keySet()) {
            if (rooms.contains(placed)) {
                requiredListPlacements.get(rooms).addPlacement();
                DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_REQUIRED_PLACEMENTS, "requirement found in list:{}",placed);
                if (requiredListPlacements.get(rooms).max>=0)
                    DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}-{}",requiredListPlacements.get(rooms).placed,requiredListPlacements.get(rooms).min,requiredListPlacements.get(rooms).max);
                else
                    DebugLog.logInfo(DebugLog.DebugLevel.GENERATING_REQUIRED_PLACEMENTS, "total placed:{}/{}",requiredListPlacements.get(rooms).placed,requiredListPlacements.get(rooms).min);
            }
        }
    }

    public GridRoom getStartingRoom() {
        if (startingRoom != null)
            return startingRoom.getCopy();
        return null;
    }

    /**
     * Checks whether all required room and room group placement constraints have been satisfied.
     *
     * @return true if all individual and grouped required rooms have met their minimum placement counts; false otherwise
     */
    public boolean requiredRoomsDone() {
        for (GridRoom room : requiredPlacements.keySet()) {
            if (!requiredPlacements.get(room).placementAboveMin())
                return false;
        }
        for (List<GridRoom> rooms : requiredListPlacements.keySet()) {
            if (!requiredListPlacements.get(rooms).placementAboveMin())
                return false;
        }
        return true;
    }

    public StructureProcessorList getStructureProcessors() {
        return structureProcessors;
    }

    public static class RequiredRoomPlacements {
        private final int min;
        private final int max;
        private int placed = 0;

        RequiredRoomPlacements(int min, int max) {
            this.min = min;
            this.max = max;
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
