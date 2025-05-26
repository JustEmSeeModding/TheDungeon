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
import net.minecraft.world.phys.Vec3;

import java.util.*;

public abstract class GridRoomCollection {
    public final GridRoomCollection instance;

    //private final List<GridRoom> allGridRooms = new ArrayList<>();
    private final Map<GridRoom, Integer> allGridRooms = new HashMap<>();
    private final List<ConnectionRule> connectionRules = new ArrayList<>();
    private final List<FailRule> failRules = new ArrayList<>();
    /** the vector stands for: (min, max, totalPlaced)*/
    private final Map<GridRoom, Vec3i> requiredPlacements = new HashMap<>();
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

    // construction methods


    public GridRoomCollection addRoom(GridRoom gridRoom) {
        if (gridRoom.getGridWidth() != roomWidth || gridRoom.getGridHeight() != roomHeight) {
            TheDungeon.LOGGER.error("Room ({}:{},{}) is not the same size as the RoomCollection ({}:{},{})", gridRoom, gridRoom.getGridWidth(), gridRoom.getGridHeight(), this, roomWidth, roomHeight);
            return this;
        }
        int weight = gridRoom.getWeight();
        allGridRooms.put(gridRoom, weight);

        return this;
    }

    @Deprecated
    private GridRoomCollection SetRooms(List<GridRoom> rooms) {
        //allGridRooms.clear();
        //allGridRooms.addAll(rooms);
        return null;
    }

    public GridRoomCollection setStartingRoom(GridRoom gridRoom) {
        if (gridRoom == null) return this;
        if (gridRoom.getGridWidth() != roomWidth || gridRoom.getGridHeight() != roomHeight) {
            TheDungeon.LOGGER.error("Room ({}:{},{}) is not the same size as the RoomCollection ({}:{},{})", gridRoom, gridRoom.getGridWidth(), gridRoom.getGridHeight(), this, roomWidth, roomHeight);
            return this;
        }
        startingRoom = (gridRoom);
        return this;
    }

    /**
     * the minimum and maximum times a room has to generate
     */
    public GridRoomCollection addRequiredRoom(int requiredAmount, int maxAmount, GridRoom gridRoom) {
        if (gridRoom.getGridWidth() != roomWidth || gridRoom.getGridHeight() != roomHeight) {
            TheDungeon.LOGGER.error("Room ({}:{},{}) is not the same size as the RoomCollection ({}:{},{})", gridRoom, gridRoom.getGridWidth(), gridRoom.getGridHeight(), this, roomWidth, roomHeight);
            return this;
        }

        addRoom(gridRoom);
        requiredPlacements.put(gridRoom, new Vec3i(requiredAmount, maxAmount, 0));

        return this;
    }


    /**
     * the minimum times a room has to generate, has no maximum
     */
    public GridRoomCollection addRequiredRoom(int requiredAmount, GridRoom room) {
        return addRequiredRoom(requiredAmount, -1, room);
    }

    private GridRoomCollection setRequiredRooms(Map<GridRoom, Vec3i> map) {
        requiredPlacements.clear();
        requiredPlacements.putAll(map);
        return this;
    }

    public GridRoomCollection setFallback(GridRoom fallbackGridRoom) {
        if (fallbackGridRoom.getGridWidth() != roomWidth || fallbackGridRoom.getGridHeight() != roomHeight) {
            TheDungeon.LOGGER.error("fallbackRoom ({}:{},{}) is not the same size as the RoomCollection ({}:{},{})", fallbackGridRoom, fallbackGridRoom.getGridWidth(), fallbackGridRoom.getGridHeight(), this, roomWidth, roomHeight);
            return this;
        }
        if (fallbackGridRoom.getHeightScale() > 1 || fallbackGridRoom.getRotatedEastSizeScale(Rotation.NONE) > 1 || fallbackGridRoom.getRotatedNorthSizeScale(Rotation.NONE) > 1) {
            TheDungeon.LOGGER.error("fallbackRoom ({}) is a large grid room in RoomCollection ({})", fallbackGridRoom, this);
            return this;
        }
        this.fallbackGridRoom = fallbackGridRoom;
        return this;
    }

    private GridRoomCollection setConnectionRules(List<ConnectionRule> rules) {
        connectionRules.clear();
        connectionRules.addAll(rules);
        return this;
    }

    private GridRoomCollection setFailRules(List<FailRule> rules) {
        failRules.clear();
        failRules.addAll(rules);
        return this;
    }

    /**
     * only use if you know what you are doing
     */
    public GridRoomCollection forceSetFallback(GridRoom fallbackGridRoom) {
        this.fallbackGridRoom = fallbackGridRoom;
        return this;
    }

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
            if (allowed) {
                toReturn.put(room, room.getWeight());
            }
        }
        return toReturn;
    }

    public abstract GridRoomCollection getCopy();

    public void updatePlacedRequirements(GridRoom placed) {
        //TheDungeon.LOGGER.warn("required called" + placed);
        //TheDungeon.LOGGER.warn("required list +" + ListAndArrayUtils.mapToString(requiredPlacements));
        if (requiredPlacements.containsKey(placed)) {
            //TheDungeon.LOGGER.warn("placed required");
            requiredPlacements.put(placed, requiredPlacements.get(placed).offset(0, 0, 1));
        }
    }

    public GridRoom getStartingRoom() {
        if (startingRoom != null)
            return startingRoom.getCopy();
        return null;
    }

    public boolean requiredRoomsDone() {
        for (GridRoom room : requiredPlacements.keySet()) {
            if (requiredPlacements.get(room).getX() > requiredPlacements.get(room).getZ())
                return false;
        }
        return true;
    }

    public StructureProcessorList getStructureProcessors() {
        return structureProcessors;
    }
}
