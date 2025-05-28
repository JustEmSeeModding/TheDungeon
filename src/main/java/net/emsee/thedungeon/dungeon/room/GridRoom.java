package net.emsee.thedungeon.dungeon.room;

import com.ibm.icu.impl.Pair;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.mobSpawnRules.MobSpawnRules;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;

import static net.emsee.thedungeon.dungeon.connectionRules.ConnectionRule.DEFAULT_CONNECTION_TAG;
import static net.emsee.thedungeon.dungeon.room.GridRoomUtils.*;

public class GridRoom {
    protected ResourceLocation resourceLocation;
    protected final int gridWidth;
    protected final int gridHeight;
    protected int weight = 1;
    protected int generationPriority = 0;
    protected int northSizeScale = 1; //Z
    protected int eastSizeScale = 1;  //X
    protected int heightScale = 1;//Y
    protected float overrideEndChance = 0;
    protected boolean doOverrideEndChance = false;
    protected boolean allowRotation = false;
    protected boolean allowUpDownConnectedRotation = false;

    /* Priority map */
    protected final Map<GridRoomUtils.Connection, Integer> connections = new HashMap<>();
    /* Offset map */
    protected final Map<GridRoomUtils.Connection, Pair<Integer, Integer>> connectionOffsets = new HashMap<>();
    /* Weighted map */
    protected final Map<GridRoomUtils.Connection, String> connectionTags = new HashMap<>();
    protected final List<MobSpawnRules> spawnRules = new ArrayList<>();
    protected final StructureProcessorList structureProcessors = new StructureProcessorList(new ArrayList<>());

    // for when the room is the same, but it requires a different equals and hash
    protected final int differentiationID;

    //// constructor

    public GridRoom(int gridWidth, int gridHeight) {
        this (gridWidth, gridHeight, 0);
    }

    public GridRoom(int gridWidth, int gridHeight, int ID) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        for (Connection connection : Connection.values()) {
            connectionTags.put(connection, DEFAULT_CONNECTION_TAG);
            connections.put(connection, 0);
        }

        differentiationID=ID;
    }

    //// construction methods

    public GridRoom withResourceLocation(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        return this;
    }

    public GridRoom withResourceLocation(String path) {
        return this.withResourceLocation(TheDungeon.MOD_ID, path);
    }

    public GridRoom withResourceLocation(String nameSpace, String path) {
        return this.withResourceLocation(ResourceLocation.fromNamespaceAndPath(nameSpace, path));
    }

    public GridRoom setConnections(boolean north, boolean east, boolean south, boolean west, boolean up, boolean down) {
        return setConnections(north?1:0,east?1:0,south?1:0,west?1:0,up?1:0,down?1:0);
    }

    public GridRoom setConnections(int northPriority, int eastPriority, int southPriority, int westPriority, int upPriority, int downPriority) {
        horizontalConnections(northPriority, eastPriority, southPriority, westPriority);
        addConnection(Connection.UP, upPriority);
        addConnection(Connection.DOWN, downPriority);
        return this;
    }


    protected GridRoom setConnections(Map<Connection, Integer> connectionMap) {
        this.connections.putAll(connectionMap);
        return this;
    }

    public GridRoom allConnections() {
        return setConnections(1, 1, 1, 1, 1, 1);
    }

    public GridRoom horizontalConnections() {
        return horizontalConnections(1,1,1,1);
    }

    public GridRoom horizontalConnections(int northPriority, int eastPriority, int southPriority, int westPriority) {
        addConnection(Connection.NORTH, northPriority);
        addConnection(Connection.EAST, eastPriority);
        addConnection(Connection.SOUTH, southPriority);
        addConnection(Connection.WEST, westPriority);
        return this;
    }

    public GridRoom addConnection(GridRoomUtils.Connection connection) {
        return addConnection(connection,1);
    }

    public GridRoom addConnection(GridRoomUtils.Connection connection, int priority) {
        connections.put(connection, priority);
        return this;
    }

    public GridRoom removeConnection(GridRoomUtils.Connection connection) {
        if (hasConnection(connection))
            connections.put(connection, 0);
        return this;
    }

    public GridRoom withWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public GridRoom doAllowRotation() {
        return doAllowRotation(true, true);
    }

    public GridRoom doAllowRotation(boolean allow, boolean upDown) {
        allowRotation = allow;
        allowUpDownConnectedRotation = allow && upDown;
        return this;
    }

    public GridRoom setSize(int north, int east) {
        if (north % 2 == 1 && east % 2 == 1) {
            this.northSizeScale = north;
            this.eastSizeScale = east;
            return this;
        } else
            throw new IllegalStateException("east or north size scale must be odd");
    }

    public GridRoom setHeight(int height) {
        this.heightScale = height;
        return this;
    }

    public GridRoom setSizeHeight(int northSize, int eastSize, int height) {
        return this.setSize(northSize, eastSize).setHeight(height);
    }

    /**
     * offsets a specific connection along its horizontal face
     * the offset is as viewed from the outside (-=left +=right)
     */
    public GridRoom setHorizontalConnectionOffset(GridRoomUtils.Connection connection, int widthOffset, int heightOffset) {
        if (Mth.abs(widthOffset)>(northSizeScale-1)/2 || heightOffset>(heightScale-1) || heightOffset<0)
            throw new IllegalStateException("offset is more than the room size");
        if (connection == GridRoomUtils.Connection.UP || connection == GridRoomUtils.Connection.DOWN) return this;
        connectionOffsets.put(connection, Pair.of(widthOffset, heightOffset));
        return this;
    }

    /**
     * offsets a specific connection along its vertical face
     */
    public GridRoom setVerticalConnectionOffset(GridRoomUtils.Connection connection, int northOffset, int eastOffset) {
        if (Mth.abs(northOffset)>(northSizeScale-1)/2 || Mth.abs(eastOffset)>(eastSizeScale-1)/2)
            throw new IllegalStateException("offset is more than the room size");
        if (connection == GridRoomUtils.Connection.NORTH || connection == GridRoomUtils.Connection.EAST || connection == GridRoomUtils.Connection.SOUTH || connection == GridRoomUtils.Connection.WEST)
            return this;
        connectionOffsets.put(connection, Pair.of(northOffset, eastOffset));
        return this;
    }

    public GridRoom setConnectionTag(Connection connection, String tag) {
        connectionTags.put(connection, tag);
        return this;
    }

    protected GridRoom setOffsets(Map<GridRoomUtils.Connection, Pair<Integer, Integer>> offsets) {
        connectionOffsets.clear();
        connectionOffsets.putAll(offsets);
        return this;
    }

    protected GridRoom setConnectionTags(Map<GridRoomUtils.Connection, String> tags) {
        connectionTags.clear();
        connectionTags.putAll(tags);
        return this;
    }

    public GridRoom setAllConnectionTags(String tag) {
        connectionTags.replaceAll((c, v) -> tag);
        return this;
    }


    /**
     * the priority for this room to generate the next connecting rooms over another
     */
    public GridRoom setGenerationPriority(int generationPriority) {
        this.generationPriority = generationPriority;
        return this;
    }

    /**
     * overrides the dungeons room end chance for this room
     */
    public GridRoom setOverrideEndChance(float value) {
        overrideEndChance = value;
        doOverrideEndChance = true;
        return this;
    }

    protected GridRoom setOverrideEndChance(float value, boolean doOverride) {
        overrideEndChance = value;
        doOverrideEndChance = doOverride;
        return this;
    }


    public GridRoom addMobSpawnRule(MobSpawnRules rule) {
        spawnRules.add(rule);
        return this;
    }

    protected GridRoom setSpawnRules(List<MobSpawnRules> list) {
        spawnRules.clear();
        spawnRules.addAll(list);
        return this;
    }

    public GridRoom withStructureProcessor(StructureProcessor processor) {
        this.structureProcessors.list().add(processor);
        return this;
    }

    protected GridRoom setStructureProcessors(StructureProcessorList processors) {
        this.structureProcessors.list().clear();
        this.structureProcessors.list().addAll(processors.list());
        return this;
    }
    // methods

    /**
     * gets all rotations that give the room a connection at the given face
     */
    public List<Rotation> getAllowedRotations(GridRoomUtils.Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        List<Rotation> toReturn = new ArrayList<>();
        if (!allowRotation) {
            toReturn.add(Rotation.NONE);
            return toReturn;
        }

        if ((connection == GridRoomUtils.Connection.UP && ConnectionRule.isValid(fromTag, connectionTags.get(GridRoomUtils.Connection.UP), connectionRules)) ||
                (connection == GridRoomUtils.Connection.DOWN && ConnectionRule.isValid(fromTag, connectionTags.get(GridRoomUtils.Connection.UP), connectionRules))) {
            toReturn.add(Rotation.NONE);
            toReturn.add(Rotation.COUNTERCLOCKWISE_90);
            toReturn.add(Rotation.CLOCKWISE_180);
            toReturn.add(Rotation.CLOCKWISE_90);
            return toReturn;
        }


        if (connections.get(connection)>0 &&
                isValidConnection(connection, Rotation.NONE, fromTag, connectionRules))
            toReturn.add(Rotation.NONE);
        if (getRotatedConnections(connections, Rotation.COUNTERCLOCKWISE_90).get(connection)>0 &&
                isValidConnection(connection, Rotation.COUNTERCLOCKWISE_90, fromTag, connectionRules))
            toReturn.add(Rotation.COUNTERCLOCKWISE_90);
        if (getRotatedConnections(connections, Rotation.CLOCKWISE_90).get(connection)>0 &&
                isValidConnection(connection, Rotation.CLOCKWISE_90, fromTag, connectionRules))
            toReturn.add(Rotation.CLOCKWISE_90);
        if (getRotatedConnections(connections, Rotation.CLOCKWISE_180).get(connection)>0 &&
                isValidConnection(connection, Rotation.CLOCKWISE_180, fromTag, connectionRules))
            toReturn.add(Rotation.CLOCKWISE_180);
        return toReturn;
    }


    public int getWeight() {
        return weight;
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public Map<GridRoomUtils.Connection, Integer> getConnections() {
        return new HashMap<>(connections);
    }

    protected boolean hasConnection(GridRoomUtils.Connection connection) {
        return connections.get(connection)>0;
    }

    public boolean hasConnection(GridRoomUtils.Connection connection, String withTag) {
        return connections.get(connection)>0 && connectionTags.get(connection).equals(withTag);
    }

    public boolean hasConnection(GridRoomUtils.Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        return connections.get(connection)>0 && ConnectionRule.isValid(fromTag, connectionTags.get(connection), connectionRules);
    }

    /**
     * checks if a room can be placed to accommodate a connection, also checks for all rotated instances
     */
    public boolean isAllowedPlacementConnection(GridRoomUtils.Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        if (allowRotation && connection != GridRoomUtils.Connection.UP && connection != GridRoomUtils.Connection.DOWN) {
            return hasConnection(GridRoomUtils.Connection.NORTH, fromTag, connectionRules) ||
                    hasConnection(GridRoomUtils.Connection.EAST, fromTag, connectionRules) ||
                    hasConnection(GridRoomUtils.Connection.SOUTH, fromTag, connectionRules) ||
                    hasConnection(GridRoomUtils.Connection.WEST, fromTag, connectionRules);
        }
        return hasConnection(connection, fromTag, connectionRules);
    }

    public int getHeightScale() {
        return heightScale;
    }

    public Pair<Integer, Integer> getRotatedSizeScale(Rotation rotation) {
        if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180)
            return Pair.of(northSizeScale, eastSizeScale);
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
            return Pair.of(eastSizeScale, northSizeScale);
        return null;
    }

    public int getRotatedNorthSizeScale(Rotation rotation) {
        return getRotatedSizeScale(rotation).first;
    }

    public int getRotatedEastSizeScale(Rotation rotation) {
        return getRotatedSizeScale(rotation).second;
    }

    protected int getRotatedNorthPlacementOffset(Rotation rotation) {
        return (getRotatedNorthSizeScale(rotation) + 1) / 2;
    }

    protected int getRotatedEastPlacementOffset(Rotation rotation) {
        return (getRotatedEastSizeScale(rotation) + 1) / 2;
    }

    public Vec3i getConnectionPlaceOffsets(GridRoomUtils.Connection fromConnection, Rotation placementRotation) {
        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;
        GridRoomUtils.Connection unrotatedConnection = GridRoomUtils.getRotatedConnection(fromConnection, getInvertedRotation(placementRotation));

        if (connectionOffsets.containsKey(unrotatedConnection)) {
            Pair<Integer, Integer> offset = connectionOffsets.get(unrotatedConnection);
            int first = offset.first;
            int second = offset.second;

            yOffset -= second;
            switch (fromConnection) {
                case NORTH -> xOffset += first;
                case EAST -> zOffset += first;
                case SOUTH -> xOffset -= first;
                case WEST -> zOffset -= first;

                case UP, DOWN -> {
                    yOffset += second;
                    switch (placementRotation) {
                        case CLOCKWISE_90 -> {
                            xOffset -= first;
                            zOffset -= second;
                        }
                        case CLOCKWISE_180 -> {
                            zOffset -= first;
                            xOffset += second;
                        }
                        case COUNTERCLOCKWISE_90 -> {
                            xOffset += first;
                            zOffset += second;
                        }
                        default -> {
                            zOffset += first;
                            xOffset -= second;
                        }
                    }
                }
            }
        }
        return new Vec3i(xOffset, yOffset, zOffset);
    }


    /**
     * Creates a copy of this GridRoom.
     */
    public GridRoom getCopy() {
        return new GridRoom(gridWidth, gridHeight, differentiationID).
                setSizeHeight(northSizeScale, eastSizeScale, heightScale).
                setOffsets(connectionOffsets).
                setConnectionTags(connectionTags).
                withResourceLocation(resourceLocation).
                setConnections(connections).
                doAllowRotation(allowRotation, allowUpDownConnectedRotation).
                withWeight(weight).
                setGenerationPriority(generationPriority).
                setOverrideEndChance(overrideEndChance, doOverrideEndChance).
                setSpawnRules(spawnRules).
                setStructureProcessors(structureProcessors);

    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GridRoom otherRoom) {
            return
                    resourceLocation == otherRoom.resourceLocation &&
                            gridWidth == otherRoom.gridWidth &&
                            gridHeight == otherRoom.gridHeight &&
                            ListAndArrayUtils.mapEquals(connections, otherRoom.connections) &&
                            weight == otherRoom.weight &&
                            allowRotation == otherRoom.allowRotation &&
                            allowUpDownConnectedRotation == otherRoom.allowUpDownConnectedRotation &&
                            northSizeScale == otherRoom.northSizeScale &&
                            eastSizeScale == otherRoom.eastSizeScale &&
                            heightScale == otherRoom.heightScale &&
                            connectionOffsets.equals(otherRoom.connectionOffsets) &&
                            connectionTags.equals(otherRoom.connectionTags) &&
                            generationPriority == otherRoom.generationPriority &&
                            overrideEndChance == otherRoom.overrideEndChance &&
                            doOverrideEndChance == otherRoom.doOverrideEndChance &&
                            ListAndArrayUtils.listEquals(spawnRules, otherRoom.spawnRules) &&
                            ListAndArrayUtils.listEquals(structureProcessors.list(), otherRoom.structureProcessors.list()) &&
                            differentiationID == otherRoom.differentiationID;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (resourceLocation != null ? resourceLocation.hashCode() : 0);
        result = 31 * result + gridWidth;
        result = 31 * result + gridHeight;
        result = 31 * result + connections.hashCode();
        result = 31 * result + weight;
        result = 31 * result + (allowRotation ? 1 : 0);
        result = 31 * result + (allowUpDownConnectedRotation ? 1 : 0);
        result = 31 * result + Double.hashCode(northSizeScale);
        result = 31 * result + Double.hashCode(eastSizeScale);
        result = 31 * result + Double.hashCode(heightScale);
        result = 31 * result + connectionOffsets.hashCode();
        result = 31 * result + connectionTags.hashCode();
        result = 31 * result + generationPriority;
        result = 31 * result + Double.hashCode(overrideEndChance);
        result = 31 * result + (doOverrideEndChance ? 1 : 0);
        result = 31 * result + spawnRules.hashCode();
        result = 31 * result + structureProcessors.list().hashCode();
        result = 31 * result + differentiationID;
        return result;
    }

    private boolean isValidConnection(Connection connection, Rotation placementRotation, String fromTag, List<ConnectionRule> rules) {
        return ConnectionRule.isValid(fromTag, connectionTags.get(getRotatedConnection(connection, getInvertedRotation(placementRotation))), rules);
    }

    public String getConnectionTag(Connection connection, Rotation placedRotation) {
        return connectionTags.get(getRotatedConnection(connection, getInvertedRotation(placedRotation)));
    }

    public int getMaxSizeScale() {
        return Math.max(northSizeScale, eastSizeScale);
    }

    public int getMinSizeScale() {
        return Math.min(northSizeScale, eastSizeScale);
    }

    @Override
    public String toString() {
        return resourceLocation.toString();
    }

    public ResourceLocation getResourceLocation(Random random) {
        return resourceLocation;
    }

    public boolean canUpDownRotate() {
        return allowUpDownConnectedRotation;
    }

    public int getGenerationPriority() {
        return generationPriority;
    }

    public boolean hasOverrideEndChance() {
        return doOverrideEndChance;
    }

    public float getOverrideEndChance() {
        return overrideEndChance;
    }

    public List<MobSpawnRules> getSpawnRules() {
        return new ArrayList<>(spawnRules);
    }

    public boolean hasMobSpawns() {
        return !getSpawnRules().isEmpty();
    }

    public StructureProcessorList getStructureProcessors() {return structureProcessors;}
}

