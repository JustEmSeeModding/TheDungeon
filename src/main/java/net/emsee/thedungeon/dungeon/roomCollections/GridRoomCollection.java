package net.emsee.thedungeon.dungeon.roomCollections;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.connectionRules.FailRule;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;

public abstract class GridRoomCollection {
    public final GridRoomCollection instance;

    //private final List<GridRoom> allGridRooms = new ArrayList<>();
    private final Map<GridRoom, Integer> allGridRooms = new HashMap<>();
    private final List<ConnectionRule> connectionRules = new ArrayList<>();
    private final List<FailRule> failRules = new ArrayList<>();
    /** the vector stands for: (min, max, totalPlaced)*/
    private final Map<GridRoom, Vec3i> requiredPlacements = new HashMap<>();
    private final Map<List<GridRoom>, Vec3i> requiredListPlacements = new HashMap<>();
    private GridRoom fallbackGridRoom = null;
    private GridRoom startingRoom = null;
    private final int roomWidth;
    private final int roomHeight;
    private final StructureProcessorList structureProcessors = new StructureProcessorList(new ArrayList<>());

    // constructor

    protected GridRoomCollection(int roomWidth, int roomHeight) {
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.instance=this;
    }

    /****
     * Adds a room to the collection if its dimensions match the collection's required width and height.
     *
     * @param gridRoom the room to add
     * @return this collection instance for method chaining
     */


    public GridRoomCollection addRoom(GridRoom gridRoom) {
        if (gridRoom.getGridWidth() != roomWidth || gridRoom.getGridHeight() != roomHeight)
            throw new IllegalStateException("Room "+gridRoom+" is not the same size as the RoomCollection "+this);
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
        if (gridRoom.getGridWidth() != roomWidth || gridRoom.getGridHeight() != roomHeight)
            throw new IllegalStateException("Room "+gridRoom+" is not the same size as the RoomCollection "+this);
        startingRoom = (gridRoom);
        return this;
    }

    /****
     * Adds a room to the collection with specified minimum and maximum placement requirements.
     *
     * The room will be considered required and must be placed at least `requiredAmount` times and at most `maxAmount` times during dungeon generation. The room must match the collection's dimensions; otherwise, it is not added.
     *
     * @param requiredAmount the minimum number of times the room must be placed
     * @param maxAmount the maximum number of times the room can be placed
     * @param room the room to add with placement constraints
     * @return this collection instance for chaining
     */
    public GridRoomCollection addRequiredRoom(int requiredAmount, int maxAmount, GridRoom room) {
        if (room.getGridWidth() != roomWidth || room.getGridHeight() != roomHeight)
            throw new IllegalStateException("Room "+room+" is not the same size as the RoomCollection "+this);

        addRoom(room);
        requiredPlacements.put(room, new Vec3i(requiredAmount, maxAmount, 0));

        return this;
    }


    /**
     * Adds a required room with a minimum placement count and no maximum constraint.
     *
     * @param requiredAmount the minimum number of times the room must be placed
     * @param room the room to be added as required
     * @return this collection for chaining
     */
    public GridRoomCollection addRequiredRoom(int requiredAmount, GridRoom room) {
        return addRequiredRoom(requiredAmount, -1, room);
    }

    /**
     * Adds a group of rooms with shared placement constraints, where placing any one room counts toward the group's required and maximum placement totals.
     *
     * @param requiredAmount the minimum number of placements required for the group
     * @param maxAmount the maximum number of placements allowed for the group
     * @param rooms the list of rooms to be tracked collectively for placement requirements
     * @return this collection instance for method chaining
     */
    public GridRoomCollection addRequiredRoomsOf(int requiredAmount, int maxAmount, List<GridRoom> rooms) {
        requiredListPlacements.put(rooms, new Vec3i(requiredAmount, maxAmount, 0));
        for (GridRoom room : rooms)
            addRoom(room);
        return this;
    }

    /**
     * Adds a group of rooms with a shared minimum required placement count.
     * When any room in the list is placed, it counts toward fulfilling the group's requirement.
     *
     * @param requiredAmount the minimum number of times any room in the group must be placed
     * @param rooms the list of rooms sharing the placement requirement
     * @return this collection for chaining
     */
    public GridRoomCollection addRequiredRoomsOf(int requiredAmount, List<GridRoom> rooms) {
        return addRequiredRoomsOf(requiredAmount, -1, rooms);
    }

    /**
     * Sets the fallback room to be used when no other room fits, if it matches the collection's dimensions and is not a large-scale room.
     *
     * If the provided room does not match the required width and height, or is a large-scale room, the fallback is not set and an error is logged.
     *
     * @param fallbackGridRoom the room to set as the fallback
     * @return this collection instance for chaining
     */
    public GridRoomCollection setFallback(GridRoom fallbackGridRoom) {
        if (fallbackGridRoom.getGridWidth() != roomWidth || fallbackGridRoom.getGridHeight() != roomHeight)
            throw new IllegalStateException("Room "+fallbackGridRoom+" is not the same size as the RoomCollection "+this);
        if (fallbackGridRoom.getHeightScale() > 1 || fallbackGridRoom.getRotatedEastSizeScale(Rotation.NONE) > 1 || fallbackGridRoom.getRotatedNorthSizeScale(Rotation.NONE) > 1)
            throw new IllegalStateException("Fallback can not be larger than scale 1,1");
        this.fallbackGridRoom = fallbackGridRoom;
        return this;
    }

    /**
     * Adds a connection rule to the collection.
     *
     * @return this collection for method chaining
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

    public int getWidth() {
        return roomWidth;
    }

    public int getHeight() {
        return roomHeight;
    }

    public GridRoom getRandomRoom(Random random) {
        GridRoom toReturn = Objects.requireNonNull(ListAndArrayUtils.getRandomFromWeightedMapI(getAllPossibleRooms(), random));
        return toReturn.getCopy();
    }

    public GridRoom getRandomRoom(int maxRoomScale, Random random) {
        Map<GridRoom, Integer> returnMap = new HashMap<>();
        for (GridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                returnMap.put(gridRoom, gridRoom.getWeight());
            }
        }
        GridRoom toReturn = Objects.requireNonNull(ListAndArrayUtils.getRandomFromWeightedMapI(returnMap, random));
        return toReturn.getCopy();
    }

    public GridRoom getRandomRoomByConnection(GridRoomUtils.Connection connection, String fromTag, List<ConnectionRule> connectionRules, Random random) {
        Map<GridRoom, Integer> returnList = new HashMap<>();
        for (GridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                returnList.put(gridRoom, gridRoom.getWeight());
            }
        }
        if (!returnList.isEmpty()) {
            GridRoom toReturn = Objects.requireNonNull(ListAndArrayUtils.getRandomFromWeightedMapI(returnList, random));
            return toReturn.getCopy();
        }
        return null;
    }

    public GridRoom getRandomRoomByConnection(GridRoomUtils.Connection connection, String fromTag, List<ConnectionRule> connectionRules, int maxRoomScale, Random random) {
        Map<GridRoom, Integer> returnList = new HashMap<>();

        //possibleRooms.addAll(requiredRooms);
        for (GridRoom gridRoom : getAllPossibleRooms().keySet()) {
            if (gridRoom.isAllowedPlacementConnection(connection, fromTag, connectionRules)) {
                if (gridRoom.getMaxSizeScale() <= maxRoomScale) {
                    returnList.put(gridRoom, gridRoom.getWeight());
                }
            }
        }
        if (!returnList.isEmpty()) {
            GridRoom toReturn = Objects.requireNonNull(ListAndArrayUtils.getRandomFromWeightedMapI(returnList, random));
            return toReturn.getCopy();
        }
        return null;
    }

    /**
     * Returns a map of all rooms that are eligible for placement based on individual and grouped placement constraints.
     *
     * Only rooms that have not exceeded their maximum allowed placements, either individually or as part of a required group, are included.
     *
     * @return a map of eligible GridRoom instances to their weights
     */
    private Map<GridRoom, Integer> getAllPossibleRooms() {
        Map<GridRoom, Integer> toReturn = new HashMap<>();
        for (GridRoom room : allGridRooms.keySet()) {
            boolean allowed = true;
            if (requiredPlacements.containsKey(room)) {
                Vec3i constraints = requiredPlacements.get(room);
                // Skip if no maximum (y == -1)
                if (constraints.getY() != -1 && constraints.getZ() >= constraints.getY()) {
                    allowed = false;
                }
            }
            if (allowed)
                for (List<GridRoom> rooms : requiredListPlacements.keySet()) {
                    if (rooms.contains(room)) {
                        Vec3i constraints = requiredListPlacements.get(rooms);
                        // Skip if no maximum (y == -1)
                        if (constraints.getY() != -1 && constraints.getZ() >= constraints.getY()) {
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
 * Returns a deep copy of this GridRoomCollection, including all rooms, constraints, and rules.
 *
 * @return a new GridRoomCollection instance with the same configuration as this collection
 */
public abstract GridRoomCollection getCopy();

    /****
     * Updates the placement count for required rooms or required room groups when a room is placed.
     *
     * Increments the placed count for the individual room if it is tracked as a required placement,
     * or for any group of rooms in which the placed room is included.
     *
     * @param placed the room that was placed
     */
    public void updatePlacedRequirements(GridRoom placed) {
        if (requiredPlacements.containsKey(placed)) {
            requiredPlacements.put(placed, requiredPlacements.get(placed).offset(0, 0, 1));
            return;
        }
        for (List<GridRoom> rooms : requiredListPlacements.keySet()) {
            if (rooms.contains(placed))
                requiredListPlacements.put(rooms, requiredListPlacements.get(rooms).offset(0,0,1));
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
            if (requiredPlacements.get(room).getX() > requiredPlacements.get(room).getZ())
                return false;
        }
        for (List<GridRoom> rooms : requiredListPlacements.keySet()) {
            if (requiredListPlacements.get(rooms).getX() > requiredListPlacements.get(rooms).getZ())
                return false;
        }
        return true;
    }

    public StructureProcessorList getStructureProcessors() {
        return structureProcessors;
    }
}
