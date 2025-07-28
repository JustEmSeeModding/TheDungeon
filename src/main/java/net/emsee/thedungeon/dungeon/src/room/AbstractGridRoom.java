package net.emsee.thedungeon.dungeon.src.room;

import com.ibm.icu.impl.Pair;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.DungeonUtils;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.PriorityMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;
import java.util.function.Consumer;

import static net.emsee.thedungeon.dungeon.src.DungeonUtils.getInvertedRotation;
import static net.emsee.thedungeon.dungeon.src.DungeonUtils.getRotatedConnectionMap;
import static net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule.DEFAULT_CONNECTION_TAG;

public abstract class AbstractGridRoom {
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
    protected boolean skipCollectionProcessors = false;
    protected boolean skipCollectionPostProcessors = false;

    protected final PriorityMap<Connection> connections = new PriorityMap<>();
    protected final Map<Connection, Pair<Integer, Integer>> connectionOffsets = new HashMap<>(); /* Offset map */
    protected final Map<Connection, String> connectionTags = new HashMap<>();
    protected final List<MobSpawnRule> spawnRules = new ArrayList<>();
    protected final StructureProcessorList structureProcessors = new StructureProcessorList(new ArrayList<>());
    protected final StructureProcessorList structurePostProcessors = new StructureProcessorList(new ArrayList<>());

    // for when the room is the same, but it requires a different equals and hash
    protected final int differentiationID;

    //// constructor

    public AbstractGridRoom(int gridWidth, int gridHeight) {
        this(gridWidth, gridHeight, 0);
    }

    public AbstractGridRoom(int gridWidth, int gridHeight, int ID) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        for (Connection connection : Connection.values()) {
            connectionTags.put(connection, DEFAULT_CONNECTION_TAG);
            connections.put(connection, 0);
        }

        differentiationID = ID;
    }

    //// construction methods

    public AbstractGridRoom setConnections(boolean north, boolean east, boolean south, boolean west, boolean up, boolean down) {
        return setConnections(north ? 1 : 0, east ? 1 : 0, south ? 1 : 0, west ? 1 : 0, up ? 1 : 0, down ? 1 : 0);
    }

    public AbstractGridRoom setConnections(int northPriority, int eastPriority, int southPriority, int westPriority, int upPriority, int downPriority) {
        horizontalConnections(northPriority, eastPriority, southPriority, westPriority);
        addConnection(Connection.UP, upPriority);
        addConnection(Connection.DOWN, downPriority);
        return this;
    }


    protected AbstractGridRoom setConnections(PriorityMap<Connection> connectionMap) {
        this.connections.putAll(connectionMap);
        return this;
    }

    public AbstractGridRoom allConnections() {
        return setConnections(1, 1, 1, 1, 1, 1);
    }

    public AbstractGridRoom horizontalConnections() {
        return horizontalConnections(1, 1, 1, 1);
    }

    public AbstractGridRoom horizontalConnections(int northPriority, int eastPriority, int southPriority, int westPriority) {
        addConnection(Connection.NORTH, northPriority);
        addConnection(Connection.EAST, eastPriority);
        addConnection(Connection.SOUTH, southPriority);
        addConnection(Connection.WEST, westPriority);
        return this;
    }

    public AbstractGridRoom addConnection(Connection connection) {
        return addConnection(connection, 1);
    }

    public AbstractGridRoom addConnection(Connection connection, int priority) {
        connections.put(connection, priority);
        return this;
    }

    public AbstractGridRoom removeConnection(Connection connection) {
        if (hasConnection(connection))
            connections.put(connection, 0);
        return this;
    }

    public AbstractGridRoom withWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public AbstractGridRoom doAllowRotation() {
        return doAllowRotation(true, true);
    }

    public AbstractGridRoom doAllowRotation(boolean allow, boolean upDown) {
        allowRotation = allow;
        allowUpDownConnectedRotation = allow && upDown;
        return this;
    }

    public AbstractGridRoom setSize(int north, int east) {
        if (north % 2 == 1 && east % 2 == 1) {
            this.northSizeScale = north;
            this.eastSizeScale = east;
            return this;
        } else
            throw new IllegalStateException("east or north size scale must be odd");
    }

    public AbstractGridRoom setHeight(int height) {
        this.heightScale = height;
        return this;
    }

    public AbstractGridRoom setSizeHeight(int northSize, int eastSize, int height) {
        return this.setSize(northSize, eastSize).setHeight(height);
    }

    /**
     * offsets a specific connection along its horizontal face
     * the offset is as viewed from the outside (-=left +=right)
     */
    public AbstractGridRoom setHorizontalConnectionOffset(Connection connection, int widthOffset, int heightOffset) {
        if (Mth.abs(widthOffset) > (northSizeScale - 1) / 2 || heightOffset > (heightScale - 1) || heightOffset < 0)
            throw new IllegalStateException("offset is more than the room size");
        if (connection == Connection.UP || connection == Connection.DOWN) return this;
        connectionOffsets.put(connection, Pair.of(widthOffset, heightOffset));
        return this;
    }

    /**
     * offsets a specific connection along its vertical face
     */
    public AbstractGridRoom setVerticalConnectionOffset(Connection connection, int northOffset, int eastOffset) {
        if (Mth.abs(northOffset) > (northSizeScale - 1) / 2 || Mth.abs(eastOffset) > (eastSizeScale - 1) / 2)
            throw new IllegalStateException("offset is more than the room size");
        if (connection == Connection.NORTH || connection == Connection.EAST || connection == Connection.SOUTH || connection == Connection.WEST)
            return this;
        connectionOffsets.put(connection, Pair.of(northOffset, eastOffset));
        return this;
    }

    public AbstractGridRoom setConnectionTag(Connection connection, String tag) {
        connectionTags.put(connection, tag);
        return this;
    }

    protected AbstractGridRoom setOffsets(Map<Connection, Pair<Integer, Integer>> offsets) {
        connectionOffsets.clear();
        connectionOffsets.putAll(offsets);
        return this;
    }

    protected AbstractGridRoom setConnectionTags(Map<Connection, String> tags) {
        connectionTags.clear();
        connectionTags.putAll(tags);
        return this;
    }

    public AbstractGridRoom setAllConnectionTags(String tag) {
        connectionTags.replaceAll((c, v) -> tag);
        return this;
    }


    /**
     * the priority for this room to generate the next connecting rooms over another
     */
    public AbstractGridRoom setGenerationPriority(int generationPriority) {
        this.generationPriority = generationPriority;
        return this;
    }

    /**
     * overrides the dungeons room end chance for this room
     */
    public AbstractGridRoom setOverrideEndChance(float value) {
        overrideEndChance = value;
        doOverrideEndChance = true;
        return this;
    }

    protected AbstractGridRoom setOverrideEndChance(float value, boolean doOverride) {
        overrideEndChance = value;
        doOverrideEndChance = doOverride;
        return this;
    }


    public AbstractGridRoom addMobSpawnRule(MobSpawnRule rule) {
        spawnRules.add(rule);
        return this;
    }

    protected AbstractGridRoom setSpawnRules(List<MobSpawnRule> list) {
        spawnRules.clear();
        spawnRules.addAll(list);
        return this;
    }

    public AbstractGridRoom withStructureProcessor(StructureProcessor processor) {
        if (processor instanceof PostProcessor)
            throw new IllegalStateException("Adding post processor as normal processor");
        this.structureProcessors.list().add(processor);
        return this;
    }

    public AbstractGridRoom withStructurePostProcessor(StructureProcessor processor) {
        if (!(processor instanceof PostProcessor))
            throw new IllegalStateException("Adding normal processor as post processor");
        this.structurePostProcessors.list().add(processor);
        return this;
    }

    public AbstractGridRoom clearStructureProcessors() {
        this.structureProcessors.list().clear();
        return this;
    }

    public AbstractGridRoom clearStructurePostProcessors() {
        this.structurePostProcessors.list().clear();
        return this;
    }

    protected AbstractGridRoom setStructureProcessors(StructureProcessorList processors, StructureProcessorList postProcessors) {
        this.structureProcessors.list().clear();
        this.structureProcessors.list().addAll(processors.list());
        this.structurePostProcessors.list().clear();
        this.structurePostProcessors.list().addAll(postProcessors.list());
        return this;
    }

    public AbstractGridRoom skipCollectionProcessors() {
        skipCollectionProcessors = true;
        return this;
    }

    public AbstractGridRoom skipCollectionPostProcessors() {
        skipCollectionPostProcessors = true;
        return this;
    }

    protected AbstractGridRoom setSkipCollectionProcessors(boolean skip, boolean skipPost) {
        skipCollectionProcessors = skip;
        skipCollectionPostProcessors = skipPost;
        return this;
    }
    // methods

    /**
     * gets all rotations that give the room a connection at the given face
     */
    public List<Rotation> getAllowedRotations(Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        List<Rotation> toReturn = new ArrayList<>();
        if (!allowRotation) {
            toReturn.add(Rotation.NONE);
            return toReturn;
        }

        if ((connection == Connection.UP && ConnectionRule.isValid(fromTag, connectionTags.get(Connection.UP), connectionRules)) ||
                (connection == Connection.DOWN && ConnectionRule.isValid(fromTag, connectionTags.get(Connection.UP), connectionRules))) {
            toReturn.add(Rotation.NONE);
            toReturn.add(Rotation.COUNTERCLOCKWISE_90);
            toReturn.add(Rotation.CLOCKWISE_180);
            toReturn.add(Rotation.CLOCKWISE_90);
            return toReturn;
        }


        if (connections.get(connection) > 0 &&
                isValidConnection(connection, Rotation.NONE, fromTag, connectionRules))
            toReturn.add(Rotation.NONE);
        if (getRotatedConnectionMap(connections, Rotation.COUNTERCLOCKWISE_90).get(connection) > 0 &&
                isValidConnection(connection, Rotation.COUNTERCLOCKWISE_90, fromTag, connectionRules))
            toReturn.add(Rotation.COUNTERCLOCKWISE_90);
        if (getRotatedConnectionMap(connections, Rotation.CLOCKWISE_90).get(connection) > 0 &&
                isValidConnection(connection, Rotation.CLOCKWISE_90, fromTag, connectionRules))
            toReturn.add(Rotation.CLOCKWISE_90);
        if (getRotatedConnectionMap(connections, Rotation.CLOCKWISE_180).get(connection) > 0 &&
                isValidConnection(connection, Rotation.CLOCKWISE_180, fromTag, connectionRules))
            toReturn.add(Rotation.CLOCKWISE_180);
        return toReturn;
    }


    public int getWeight() {
        return weight;
    }

    public int getGridCellWidth() {
        return gridWidth;
    }

    public int getGridCellHeight() {
        return gridHeight;
    }

    public PriorityMap<Connection> getConnections() {
        return new PriorityMap<>(connections);
    }

    protected boolean hasConnection(Connection connection) {
        return connections.get(connection) > 0;
    }

    public boolean hasConnection(Connection connection, String withTag) {
        return connections.get(connection) > 0 && connectionTags.get(connection).equals(withTag);
    }

    public boolean hasConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        return connections.get(connection) > 0 && ConnectionRule.isValid(fromTag, connectionTags.get(connection), connectionRules);
    }

    /**
     * checks if a room can be placed to accommodate a connection, also checks for all rotated instances
     */
    public boolean isAllowedPlacementConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        if (allowRotation && connection != Connection.UP && connection != Connection.DOWN) {
            return hasConnection(Connection.NORTH, fromTag, connectionRules) ||
                    hasConnection(Connection.EAST, fromTag, connectionRules) ||
                    hasConnection(Connection.SOUTH, fromTag, connectionRules) ||
                    hasConnection(Connection.WEST, fromTag, connectionRules);
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

    public Vec3i getConnectionPlaceOffsets(Connection fromConnection, Rotation placementRotation) {
        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;
        Connection unrotatedConnection = fromConnection.getRotated(getInvertedRotation(placementRotation));

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
    public abstract AbstractGridRoom getCopy();

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    private boolean isValidConnection(Connection connection, Rotation placementRotation, String fromTag, List<ConnectionRule> rules) {
        return ConnectionRule.isValid(fromTag, DungeonUtils.getRotatedTags(connectionTags, placementRotation).get(connection), rules);
    }

    public String getConnectionTag(Connection connection, Rotation placedRotation) {
        return connectionTags.get(connection.getRotated(getInvertedRotation(placedRotation)));
    }

    public int getMaxSizeScale() {
        return Math.max(northSizeScale, eastSizeScale);
    }

    public int getMinSizeScale() {
        return Math.min(northSizeScale, eastSizeScale);
    }

    @Override
    public abstract String toString();

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

    public List<MobSpawnRule> getSpawnRules() {
        return new ArrayList<>(spawnRules);
    }

    public boolean hasMobSpawns() {
        return !getSpawnRules().isEmpty();
    }

    public final boolean doSkipCollectionProcessors() {
        return skipCollectionProcessors;
    }

    public final boolean doSkipCollectionPostProcessors() {
        return skipCollectionPostProcessors;
    }

    public StructureProcessorList getStructureProcessors() {
        return structureProcessors;
    }

    public StructureProcessorList getStructurePostProcessors() {
        return structurePostProcessors;
    }

    public boolean hasPostProcessing() {
        return !structurePostProcessors.list().isEmpty();
    }

    public abstract void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random);

    public abstract void postProcess(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList postProcessors, Random random);

    public BlockPos getMinCorner(BlockPos centre, Rotation roomRotation) {
        int XO = -(getRotatedEastSizeScale(roomRotation) / 2 * gridWidth + (gridWidth - 1) / 2);
        int ZO = -(getRotatedNorthSizeScale(roomRotation) / 2 * gridWidth + (gridWidth - 1) / 2);
        return centre.offset(XO, 0, ZO);
    }

    public BlockPos getMaxCorner(BlockPos centre, Rotation roomRotation) {
        int XO = getRotatedEastSizeScale(roomRotation) / 2 * gridWidth + (gridWidth - 1) / 2;
        int ZO = getRotatedNorthSizeScale(roomRotation) / 2 * gridWidth + (gridWidth - 1) / 2;
        return centre.offset(XO, heightScale * gridHeight-1, ZO);
    }

    public void forEachBlockPosInBounds(BlockPos centre, Rotation roomRotation, BlockUtils.ForEachMethod method, Consumer<BlockPos> consumer) {
        BlockUtils.forEachInArea(getMinCorner(centre, roomRotation), getMaxCorner(centre,roomRotation), method, consumer);
    }
}

