package net.emsee.thedungeon.dungeon.src.types.grid.room;

import net.emsee.thedungeon.dungeon.src.mobSpawnRules.MobSpawnRule;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.*;
import java.util.function.Function;

public final class GridRoomGroup extends AbstractGridRoom {
    final GroupData groupData;

    private GridRoomGroup(GroupData groupData, Data data) {
        super(data);
        this.groupData = groupData;
    }

    protected static class GroupDataBuilder {
        private final WeightedMap.Int<AbstractGridRoom.Builder<?>> builders = new WeightedMap.Int<>();

        public GroupData build() {
            GroupData toReturn = new GroupData();
            builders.forEach((builder,i) -> {
                toReturn.gridRooms.put(builder.build(),i);
            });
            return toReturn;
        }
    }

    protected static class GroupData {
        private final WeightedMap.Int<AbstractGridRoom> gridRooms = new WeightedMap.Int<>();
    }



    public static Builder builder(int gridWidth, int gridHeight) {
        return new Builder(gridWidth, gridHeight, 0);
    }

    public static Builder builder(int gridWidth, int gridHeight, int differentiationID) {
        return new Builder(gridWidth, gridHeight, differentiationID);
    }

    public static class Builder extends AbstractGridRoom.Builder<GridRoomGroup> {
        private final GroupDataBuilder groupDataBuilder;

        protected Builder(int gridWidth, int gridHeight, int differentiationID) {
            super(gridWidth, gridHeight, differentiationID);
            groupDataBuilder = new GroupDataBuilder();
        }

        protected Builder(GroupDataBuilder groupDataBuilder, Data data) {
            super(data);
            this.groupDataBuilder = groupDataBuilder;
        }

        @Override
        public GridRoomGroup build() {
            return new GridRoomGroup(groupDataBuilder.build(), data);
        }

        /**
         * Adds a new simple room to the group with the specified resource location and weight.
         */
        public Builder addSimpleRoom(String recourseLocation, int weight) {
            addRoom(
                    GridRoomBasic.builder(recourseLocation,data.gridWidth, data.gridHeight)
                            .withWeight(weight).
                            setSizeHeight(data.northSizeScale, data.eastSizeScale, data.heightScale).
                            setOffsets(data.connectionOffsets).
                            setConnectionTags(data.connectionTags).
                            setConnections(data.connections).
                            doAllowRotation(data.allowRotation, data.allowUpDownConnectedRotation).
                            setGenerationPriority(data.generationPriority).
                            setOverrideEndChance(data.overrideEndChance, data.doOverrideEndChance).
                            setSpawnRules(data.spawnRules)
            );

            return this;
        }

        /**
         * A roomGroup must always have the same connections
         */
        public Builder addRoom(AbstractGridRoom.Builder<?> gridRoom) {

            if (!ListAndArrayUtils.mapEquals(data.connections, gridRoom.data.connections))
                throw new IllegalStateException(this+":added room does not have the same connections as the group");

            if (!ListAndArrayUtils.mapEquals(data.connectionTags, gridRoom.data.connectionTags))
                throw new IllegalStateException(this+":added room does not have the same connection tags as the group");

            if (data.heightScale != gridRoom.data.heightScale || data.northSizeScale != gridRoom.data.northSizeScale || data.eastSizeScale != gridRoom.data.eastSizeScale)
                throw new IllegalStateException(this+":added room does not have the same scale as the group");

            groupDataBuilder.builders.put(gridRoom, gridRoom.data.weight);
            return this;
        }

        public Builder applyToAll(Function<AbstractGridRoom.Builder<?>, AbstractGridRoom.Builder<?>> function) {
            ListAndArrayUtils.mapForEachSafe(groupDataBuilder.builders, (e)-> Map.entry(function.apply(e.getKey()), e.getValue()));
            return this;
        }

        @Override
        public Builder withStructureProcessor(StructureProcessor processor) {
            return applyToAll(room -> room.withStructureProcessor(processor));
        }

        @Override
        public Builder withStructurePostProcessor(StructureProcessor processor) {
            return applyToAll(room -> room.withStructurePostProcessor(processor));
        }

        @Override
        public Builder clearStructureProcessors() {
            return applyToAll(AbstractGridRoom.Builder::clearStructureProcessors);
        }

        @Override
        public Builder addMobSpawnRule(MobSpawnRule rule) {
            return applyToAll(room -> room.addMobSpawnRule(rule));
        }

        @Override
        public Builder setConnectionTag(Connection connection, String tag) {
            super.setConnectionTag(connection, tag);
            return applyToAll(room->room.setConnectionTag(connection, tag));
        }

        @Override
        public Builder setAllConnectionTags(String tag) {
            super.setAllConnectionTags(tag);
            return applyToAll(room->room.setAllConnectionTags(tag));
        }


        @Override
        public Builder setGenerationPriority(int generationPriority) {
            return applyToAll(room -> room.setGenerationPriority(generationPriority));
        }

        @Override
        public Builder setOverrideEndChance(float value) {
            return applyToAll(room -> room.setOverrideEndChance(value));
        }

        @Override
        public Builder skipCollectionProcessors() {
            return applyToAll(AbstractGridRoom.Builder::skipCollectionProcessors);
        }

    }

    //methods

    @Override
    @Deprecated
    public StructureProcessorList getStructureProcessors() {
        throw new IllegalStateException(this+ ":getStructureProcessors() should not be used for groups");
    }

    @Override
    @Deprecated
    public StructureProcessorList getStructurePostProcessors() {
        throw new IllegalStateException(this+ ":getStructurePostProcessors() should not be used for groups");
    }

    @Deprecated
    @Override
    public void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {
        throw new IllegalStateException("placeFeature cant be called on a group");
    }

    @Deprecated
    @Override
    public void postProcess(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList postProcessors, Random random) {
        throw new IllegalStateException("postProcess cant be called on a group");
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomGroup otherGroup &&
                data.gridWidth == otherGroup.data.gridWidth &&
                data.gridHeight == otherGroup.data.gridHeight &&
                ListAndArrayUtils.mapEquals(data.connections, otherGroup.data.connections) &&
               data.weight == otherGroup.data.weight &&
               data.allowRotation == otherGroup.data.allowRotation &&
               data.allowUpDownConnectedRotation == otherGroup.data.allowUpDownConnectedRotation &&
               data.northSizeScale == otherGroup.data.northSizeScale &&
               data.eastSizeScale == otherGroup.data.eastSizeScale &&
               data.heightScale == otherGroup.data.heightScale &&
                ListAndArrayUtils.mapEquals(groupData.gridRooms, otherGroup.groupData.gridRooms) &&
                data.connectionOffsets.equals(otherGroup.data.connectionOffsets) &&
                data.connectionTags.equals(otherGroup.data.connectionTags) &&
                data.generationPriority == otherGroup.data.generationPriority &&
                data.overrideEndChance == otherGroup.data.overrideEndChance &&
                data.doOverrideEndChance == otherGroup.data.doOverrideEndChance &&
                ListAndArrayUtils.listEquals(data.spawnRules, otherGroup.data.spawnRules) &&
                ListAndArrayUtils.listEquals(data.structureProcessors.list(), otherGroup.data.structureProcessors.list()) &&
                data.differentiationID == otherGroup.data.differentiationID &&
                data.skipCollectionProcessors == otherGroup.data.skipCollectionProcessors;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + data.gridWidth;
        result = 31 * result + data.gridHeight;
        result = 31 * result + data.connections.hashCode();
        result = 31 * result + data.weight;
        result = 31 * result + (data.allowRotation ? 1 : 0);
        result = 31 * result + (data.allowUpDownConnectedRotation ? 1 : 0);
        result = 31 * result + Double.hashCode(data.northSizeScale);
        result = 31 * result + Double.hashCode(data.eastSizeScale);
        result = 31 * result + Double.hashCode(data.heightScale);
        result = 31 * result + groupData.gridRooms.hashCode();
        result = 31 * result + data.connectionOffsets.hashCode();
        result = 31 * result + data.connectionTags.hashCode();
        result = 31 * result + data.generationPriority;
        result = 31 * result + Double.hashCode(data.overrideEndChance);
        result = 31 * result + (data.doOverrideEndChance ? 1 : 0);
        result = 31 * result + data.spawnRules.hashCode();
        result = 31 * result + data.structureProcessors.list().hashCode();
        result = 31 * result + data.differentiationID;
        result = 31 * result + (data.skipCollectionProcessors ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "roomGroup, rooms:" + ListAndArrayUtils.mapToString(groupData.gridRooms);
    }

    public AbstractGridRoom getRandom(Random random) {
        AbstractGridRoom toReturn = groupData.gridRooms.getRandom(random);
        if (toReturn == null)
            throw new IllegalStateException("error choosing room");
        return toReturn;
    }
}
