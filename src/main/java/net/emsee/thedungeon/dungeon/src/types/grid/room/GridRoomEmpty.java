package net.emsee.thedungeon.dungeon.src.types.grid.room;

import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Random;


/**
 * mainly used for the use of PostProcessing
 */
public final class GridRoomEmpty extends AbstractGridRoom{
    private GridRoomEmpty(Data data) {
        super(data);
    }

    public static Builder builder(int gridWidth, int gridHeight) {
        return new Builder(gridWidth, gridHeight, 0);
    }

    public static Builder builder(int gridWidth, int gridHeight, int differentiationID) {
        return new Builder(gridWidth, gridHeight, differentiationID);
    }
    
    public static class Builder extends AbstractGridRoom.Builder<GridRoomEmpty> {
        protected Builder(int gridWidth, int gridHeight, int differentiationID) {
            super(gridWidth, gridHeight, differentiationID);
        }

        private Builder(Data data) {
            super(data);
        }

        @Override
        public GridRoomEmpty build() {
            return new GridRoomEmpty(data);
        }

        @Deprecated
        @Override
        public AbstractGridRoom.Builder<GridRoomEmpty> clearStructureProcessors() {
            throw new IllegalStateException("clearStructureProcessors should not be used on GridRoomEmpty");
        }

        @Deprecated
        @Override
        public Builder skipCollectionProcessors() {
            throw new IllegalStateException("skipCollectionProcessors should not be used on GridRoomEmpty");
        }

        @Deprecated
        @Override
        public Builder withStructureProcessor(StructureProcessor processor) {
            throw new IllegalStateException("withStructureProcessor should not be used on GridRoomEmpty");
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomEmpty otherRoom &&
                data.gridWidth == otherRoom.data.gridWidth &&
                data.gridHeight == otherRoom.data.gridHeight &&
                ListAndArrayUtils.mapEquals(data.connections, otherRoom.data.connections) &&
                data.weight == otherRoom.data.weight &&
                data.allowRotation == otherRoom.data.allowRotation &&
                data.allowUpDownConnectedRotation == otherRoom.data.allowUpDownConnectedRotation &&
                data.northSizeScale == otherRoom.data.northSizeScale &&
                data.eastSizeScale == otherRoom.data.eastSizeScale &&
                data.heightScale == otherRoom.data.heightScale &&
                data.connectionOffsets.equals(otherRoom.data.connectionOffsets) &&
                data.connectionTags.equals(otherRoom.data.connectionTags) &&
                data.generationPriority == otherRoom.data.generationPriority &&
                data.overrideEndChance == otherRoom.data.overrideEndChance &&
                data.doOverrideEndChance == otherRoom.data.doOverrideEndChance &&
                ListAndArrayUtils.listEquals(data.spawnRules, otherRoom.data.spawnRules) &&
                ListAndArrayUtils.listEquals(data.structurePostProcessors.list(), otherRoom.data.structurePostProcessors.list()) &&
                data.differentiationID == otherRoom.data.differentiationID &&
                data.skipCollectionPostProcessors == otherRoom.data.skipCollectionPostProcessors;
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
        result = 31 * result + data.connectionOffsets.hashCode();
        result = 31 * result + data.connectionTags.hashCode();
        result = 31 * result + data.generationPriority;
        result = 31 * result + Double.hashCode(data.overrideEndChance);
        result = 31 * result + (data.doOverrideEndChance ? 1 : 0);
        result = 31 * result + data.spawnRules.hashCode();
        result = 31 * result + data.structureProcessors.list().hashCode();
        result = 31 * result + data.structurePostProcessors.list().hashCode();
        result = 31 * result + data.differentiationID;
        result = 31 * result + (data.skipCollectionPostProcessors ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmptyGridRoom";
    }

    @Override
    public void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {}

    @Override
    public void postProcess(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList postProcessors, Random random) {
        for (StructureProcessor processor : postProcessors.list()) {
            if (processor instanceof PostProcessor postProcessorData)
                forEachBlockPosInBounds(centre, roomRotation, postProcessorData.getMethod(),serverLevel, pos -> {
                    BlockState initialState = serverLevel.getBlockState(pos);
                    // Create a StructureBlockInfo for the block
                    StructureTemplate.StructureBlockInfo blockInfo = new StructureTemplate.StructureBlockInfo(
                            pos,
                            initialState,
                            null
                    );

                    blockInfo = processor.process(serverLevel, new BlockPos(0, 0, 0), pos, blockInfo, blockInfo, new StructurePlaceSettings(), null);

                    // Place processed block state (or fallback to initial state)
                    serverLevel.setBlockAndUpdate(pos, blockInfo != null ? blockInfo.state() : initialState);
                });
            else throw new IllegalStateException("Adding normal processor as post processor");
        }
    }
}
