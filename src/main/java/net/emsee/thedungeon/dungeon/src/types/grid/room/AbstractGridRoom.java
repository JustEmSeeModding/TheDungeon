package net.emsee.thedungeon.dungeon.src.types.grid.room;

import com.ibm.icu.impl.Pair;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.DungeonUtils;
import net.emsee.thedungeon.dungeon.src.connectionRules.ConnectionRule;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.PriorityMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;
import java.util.function.Consumer;

import static net.emsee.thedungeon.dungeon.src.DungeonUtils.getInvertedRotation;
import static net.emsee.thedungeon.dungeon.src.DungeonUtils.getRotatedConnectionMap;

public abstract class AbstractGridRoom {
    protected final Data data;
    
    //// constructor
    protected AbstractGridRoom(Data data) {
        this.data = data;
    }

    protected static class Data {
        public Data(int gridWidth, int gridHeight, int differentiationID) {
            this.gridWidth = gridWidth;
            this.gridHeight = gridHeight;
            this.differentiationID = differentiationID;
        }
        
        public final int gridWidth;
        public final int gridHeight;

        //when the room is the same, but it requires a different equals and hash
        public final int differentiationID;
        
        public int weight = 1;
        public int generationPriority = 0;
        public int northSizeScale = 1; //Z
        public int eastSizeScale = 1;  //X
        public int heightScale = 1;//Y
        public float overrideEndChance = 0;
        public boolean doOverrideEndChance = false;
        public boolean allowRotation = false;
        public boolean allowUpDownConnectedRotation = false;
        public boolean skipCollectionProcessors = false;
        public boolean skipCollectionPostProcessors = false;

        public final PriorityMap<Connection> connections = new PriorityMap<>();
        public final Map<Connection, Pair<Integer, Integer>> connectionOffsets = new HashMap<>(); /* Offset map */
        public final Map<Connection, String> connectionTags = Util.make(new HashMap<>(), map -> {
            for (Connection connection : Connection.values()) {
                map.put(connection, ConnectionRule.DEFAULT_CONNECTION_TAG);
            }
        });
        public final List<MobSpawnRule> spawnRules = new ArrayList<>();
        public final StructureProcessorList structureProcessors = new StructureProcessorList(new ArrayList<>());
        public final StructureProcessorList structurePostProcessors = new StructureProcessorList(new ArrayList<>());
    }
    
    public static abstract class Builder<T extends AbstractGridRoom> {
        final Data data;
        protected Builder(int gridWidth, int gridHeight, int differentiationID) {
            data=new Data(gridWidth,gridHeight,differentiationID);
        }

        protected Builder(Data data) {
            this.data = data;
        }

        public abstract T build();
        
        public Builder<T> setConnections(boolean north, boolean east, boolean south, boolean west, boolean up, boolean down) {
            return setConnections(north ? 1 : 0, east ? 1 : 0, south ? 1 : 0, west ? 1 : 0, up ? 1 : 0, down ? 1 : 0);
        }

        public Builder<T> setConnections(int northPriority, int eastPriority, int southPriority, int westPriority, int upPriority, int downPriority) {
            horizontalConnections(northPriority, eastPriority, southPriority, westPriority);
            addConnection(Connection.UP, upPriority);
            addConnection(Connection.DOWN, downPriority);
            return this;
        }


        protected Builder<T> setConnections(PriorityMap<Connection> connectionMap) {
            data.connections.putAll(connectionMap);
            return this;
        }

        public Builder<T> allConnections() {
            return setConnections(1, 1, 1, 1, 1, 1);
        }

        public Builder<T> horizontalConnections() {
            return horizontalConnections(1, 1, 1, 1);
        }

        public Builder<T> horizontalConnections(int northPriority, int eastPriority, int southPriority, int westPriority) {
            addConnection(Connection.NORTH, northPriority);
            addConnection(Connection.EAST, eastPriority);
            addConnection(Connection.SOUTH, southPriority);
            addConnection(Connection.WEST, westPriority);
            return this;
        }

        public Builder<T> addConnection(Connection connection) {
            return addConnection(connection, 1);
        }

        public Builder<T> addConnection(Connection connection, int priority) {
            data.connections.put(connection, priority);
            return this;
        }

        public Builder<T> removeConnection(Connection connection) {
            data.connections.put(connection, 0);
            return this;
        }

        public Builder<T> withWeight(int weight) {
            data.weight = weight;
            return this;
        }

        public Builder<T> doAllowRotation() {
            return doAllowRotation(true, true);
        }

        public Builder<T> doAllowRotation(boolean allow, boolean upDown) {
            data.allowRotation = allow;
            data.allowUpDownConnectedRotation = allow && upDown;
            return this;
        }

        public Builder<T> setSize(int north, int east) {
            if (north % 2 == 1 && east % 2 == 1) {
                data.northSizeScale = north;
                data.eastSizeScale = east;
                return this;
            } else
                throw new IllegalStateException("east or north size scale must be odd");
        }

        public Builder<T> setHeight(int height) {
            data.heightScale = height;
            return this;
        }

        public Builder<T> setSizeHeight(int northSize, int eastSize, int height) {
            return this.setSize(northSize, eastSize).setHeight(height);
        }

        /**
         * offsets a specific connection along its horizontal face
         * the offset is as viewed from the outside (-=left +=right)
         */
        public Builder<T> setHorizontalConnectionOffset(Connection connection, int widthOffset, int heightOffset) {
            if (Mth.abs(widthOffset) > (data.northSizeScale - 1) / 2 || heightOffset > (data.heightScale - 1) || heightOffset < 0)
                throw new IllegalStateException("offset is more than the room size");
            if (connection == Connection.UP || connection == Connection.DOWN) return this;
            data.connectionOffsets.put(connection, Pair.of(widthOffset, heightOffset));
            return this;
        }

        /**
         * offsets a specific connection along its vertical face
         */
        public Builder<T> setVerticalConnectionOffset(Connection connection, int northOffset, int eastOffset) {
            if (Mth.abs(northOffset) > (data.northSizeScale - 1) / 2 || Mth.abs(eastOffset) > (data.eastSizeScale - 1) / 2)
                throw new IllegalStateException("offset is more than the room size");
            if (connection == Connection.NORTH || connection == Connection.EAST || connection == Connection.SOUTH || connection == Connection.WEST)
                return this;
            data.connectionOffsets.put(connection, Pair.of(northOffset, eastOffset));
            return this;
        }

        public Builder<T> setConnectionTag(Connection connection, String tag) {
            data.connectionTags.put(connection, tag);
            return this;
        }

        protected Builder<T> setOffsets(Map<Connection, Pair<Integer, Integer>> offsets) {
            data.connectionOffsets.clear();
            data.connectionOffsets.putAll(offsets);
            return this;
        }

        protected Builder<T> setConnectionTags(Map<Connection, String> tags) {
            data.connectionTags.clear();
            data.connectionTags.putAll(tags);
            return this;
        }

        public Builder<T> setAllConnectionTags(String tag) {
            data.connectionTags.replaceAll((c, v) -> tag);
            return this;
        }


        /**
         * the priority for this room to generate the next connecting rooms over another
         */
        public Builder<T> setGenerationPriority(int generationPriority) {
            data.generationPriority = generationPriority;
            return this;
        }

        /**
         * overrides the dungeons room end chance for this room
         */
        public Builder<T> setOverrideEndChance(float value) {
            data.overrideEndChance = value;
            data.doOverrideEndChance = true;
            return this;
        }

        protected Builder<T> setOverrideEndChance(float value, boolean doOverride) {
            data.overrideEndChance = value;
            data.doOverrideEndChance = doOverride;
            return this;
        }


        public Builder<T> addMobSpawnRule(MobSpawnRule rule) {
            data.spawnRules.add(rule);
            return this;
        }

        protected Builder<T> setSpawnRules(List<MobSpawnRule> list) {
            data.spawnRules.clear();
            data.spawnRules.addAll(list);
            return this;
        }

        public Builder<T> withStructureProcessor(StructureProcessor processor) {
            if (processor instanceof PostProcessor)
                throw new IllegalStateException("Adding post processor as normal processor");
            data.structureProcessors.list().add(processor);
            return this;
        }

        public Builder<T> withStructurePostProcessor(StructureProcessor processor) {
            if (!(processor instanceof PostProcessor))
                throw new IllegalStateException("Adding normal processor as post processor");
            data.structurePostProcessors.list().add(processor);
            return this;
        }

        public Builder<T> clearStructureProcessors() {
            data.structureProcessors.list().clear();
            return this;
        }

        public Builder<T> clearStructurePostProcessors() {
            data.structurePostProcessors.list().clear();
            return this;
        }

        protected Builder<T> setStructureProcessors(StructureProcessorList processors, StructureProcessorList postProcessors) {
            data.structureProcessors.list().clear();
            data.structureProcessors.list().addAll(processors.list());
            data.structurePostProcessors.list().clear();
            data.structurePostProcessors.list().addAll(postProcessors.list());
            return this;
        }

        public Builder<T> skipCollectionProcessors() {
            data.skipCollectionProcessors = true;
            return this;
        }

        public Builder<T> skipCollectionPostProcessors() {
            data.skipCollectionPostProcessors = true;
            return this;
        }

        protected Builder<T> setSkipCollectionProcessors(boolean skip, boolean skipPost) {
            data.skipCollectionProcessors = skip;
            data.skipCollectionPostProcessors = skipPost;
            return this;
        }
    }
    // methods

    /**
     * gets all rotations that give the room a connection at the given face
     */
    public List<Rotation> getAllowedRotations(Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        List<Rotation> toReturn = new ArrayList<>();
        if (!data.allowRotation) {
            toReturn.add(Rotation.NONE);
            return toReturn;
        }

        if ((connection == Connection.UP && ConnectionRule.isValid(fromTag, data.connectionTags.get(Connection.UP), connectionRules)) ||
                (connection == Connection.DOWN && ConnectionRule.isValid(fromTag, data.connectionTags.get(Connection.UP), connectionRules))) {
            toReturn.add(Rotation.NONE);
            toReturn.add(Rotation.COUNTERCLOCKWISE_90);
            toReturn.add(Rotation.CLOCKWISE_180);
            toReturn.add(Rotation.CLOCKWISE_90);
            return toReturn;
        }

        if (data.connections.get(connection) > 0 &&
                isValidConnection(connection, Rotation.NONE, fromTag, connectionRules))
            toReturn.add(Rotation.NONE);
        if (getRotatedConnectionMap(data.connections, Rotation.COUNTERCLOCKWISE_90).get(connection) > 0 &&
                isValidConnection(connection, Rotation.COUNTERCLOCKWISE_90, fromTag, connectionRules))
            toReturn.add(Rotation.COUNTERCLOCKWISE_90);
        if (getRotatedConnectionMap(data.connections, Rotation.CLOCKWISE_90).get(connection) > 0 &&
                isValidConnection(connection, Rotation.CLOCKWISE_90, fromTag, connectionRules))
            toReturn.add(Rotation.CLOCKWISE_90);
        if (getRotatedConnectionMap(data.connections, Rotation.CLOCKWISE_180).get(connection) > 0 &&
                isValidConnection(connection, Rotation.CLOCKWISE_180, fromTag, connectionRules))
            toReturn.add(Rotation.CLOCKWISE_180);
        return toReturn;
    }


    public int getWeight() {
        return data.weight;
    }

    public int getGridCellWidth() {
        return data.gridWidth;
    }

    public int getGridCellHeight() {
        return data.gridHeight;
    }

    public PriorityMap<Connection> getConnections() {
        return new PriorityMap<>(data.connections);
    }

    protected boolean hasConnection(Connection connection) {
        return data.connections.get(connection) > 0;
    }

    public boolean hasConnection(Connection connection, String withTag) {
        if (data.connections.get(connection)==null) return false;
        return data.connections.get(connection) > 0 && data.connectionTags.get(connection).equals(withTag);
    }

    public boolean hasConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        if (data.connections.get(connection)==null) return false;
        return data.connections.get(connection) > 0 && ConnectionRule.isValid(fromTag, data.connectionTags.get(connection), connectionRules);
    }

    /**
     * checks if a room can be placed to accommodate a connection, also checks for all rotated instances
     */
    public boolean isAllowedPlacementConnection(Connection connection, String fromTag, List<ConnectionRule> connectionRules) {
        if (data.allowRotation && connection != Connection.UP && connection != Connection.DOWN) {
            return hasConnection(Connection.NORTH, fromTag, connectionRules) ||
                    hasConnection(Connection.EAST, fromTag, connectionRules) ||
                    hasConnection(Connection.SOUTH, fromTag, connectionRules) ||
                    hasConnection(Connection.WEST, fromTag, connectionRules);
        }
        return hasConnection(connection, fromTag, connectionRules);
    }

    public int getHeightScale() {
        return data.heightScale;
    }

    public Pair<Integer, Integer> getRotatedSizeScale(Rotation rotation) {
        if (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180)
            return Pair.of(data.northSizeScale, data.eastSizeScale);
        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90)
            return Pair.of(data.eastSizeScale, data.northSizeScale);
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

    /**
     * to move the connection along the wall
     */
    public Vec3i getConnectionPlaceOffsets(Connection fromConnection, Rotation placementRotation) {
        int xOffset = 0;
        int yOffset = 0;
        int zOffset = 0;
        Connection unrotatedConnection = fromConnection.getRotated(getInvertedRotation(placementRotation));

        if (data.connectionOffsets.containsKey(unrotatedConnection)) {
            Pair<Integer, Integer> offset = data.connectionOffsets.get(unrotatedConnection);
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

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    private boolean isValidConnection(Connection connection, Rotation placementRotation, String fromTag, List<ConnectionRule> rules) {
        return ConnectionRule.isValid(fromTag, DungeonUtils.getRotatedTags(data.connectionTags, placementRotation).get(connection), rules);
    }

    public String getConnectionTag(Connection connection, Rotation placedRotation) {
        return data.connectionTags.get(connection.getRotated(getInvertedRotation(placedRotation)));
    }

    public int getMaxSizeScale() {
        return Math.max(data.northSizeScale, data.eastSizeScale);
    }

    public int getMinSizeScale() {
        return Math.min(data.northSizeScale, data.eastSizeScale);
    }

    @Override
    public abstract String toString();

    public boolean canUpDownRotate() {
        return data.allowUpDownConnectedRotation;
    }

    public int getGenerationPriority() {
        return data.generationPriority;
    }

    public boolean hasOverrideEndChance() {
        return data.doOverrideEndChance;
    }

    public float getOverrideEndChance() {
        return data.overrideEndChance;
    }

    public List<MobSpawnRule> getSpawnRules() {
        return new ArrayList<>(data.spawnRules);
    }

    public boolean hasMobSpawns() {
        return !getSpawnRules().isEmpty();
    }

    public final boolean doSkipCollectionProcessors() {
        return data.skipCollectionProcessors;
    }

    public final boolean doSkipCollectionPostProcessors() {
        return data.skipCollectionPostProcessors;
    }

    public StructureProcessorList getStructureProcessors() {
        return data.structureProcessors;
    }

    public StructureProcessorList getStructurePostProcessors() {
        return data.structurePostProcessors;
    }

    public boolean hasPostProcessing() {
        return !data.structurePostProcessors.list().isEmpty();
    }

    public abstract void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random);

    public abstract void postProcess(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList postProcessors, Random random);

    public BlockPos getMinCorner(BlockPos centre, Rotation roomRotation) {
        int XO = -(getRotatedEastSizeScale(roomRotation) / 2 * data.gridWidth + (data.gridWidth - 1) / 2);
        int ZO = -(getRotatedNorthSizeScale(roomRotation) / 2 * data.gridWidth + (data.gridWidth - 1) / 2);
        return centre.offset(XO, 0, ZO);
    }

    public BlockPos getMaxCorner(BlockPos centre, Rotation roomRotation) {
        int XO = getRotatedEastSizeScale(roomRotation) / 2 * data.gridWidth + (data.gridWidth - 1) / 2;
        int ZO = getRotatedNorthSizeScale(roomRotation) / 2 * data.gridWidth + (data.gridWidth - 1) / 2;
        return centre.offset(XO, data.heightScale * data.gridHeight-1, ZO);
    }

    public void forEachBlockPosInBounds(BlockPos centre, Rotation roomRotation, BlockUtils.ForEachMethod method, Level level, Consumer<BlockPos> consumer) {
        BlockUtils.forEachInArea(getMinCorner(centre, roomRotation), getMaxCorner(centre,roomRotation), method,level, consumer);
    }
}

