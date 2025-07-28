package net.emsee.thedungeon.dungeon.src.room;

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
public class GridRoomEmpty extends AbstractGridRoom{

    public GridRoomEmpty(int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight);
    }

    public GridRoomEmpty(int gridWidth, int gridHeight, int ID) {
        super(gridWidth, gridHeight, ID);
    }

    @Override
    public AbstractGridRoom clearStructureProcessors() {
        throw new IllegalStateException("clearStructureProcessors should not be used on GridRoomEmpty");
    }

    @Override
    public AbstractGridRoom skipCollectionProcessors() {
        throw new IllegalStateException("skipCollectionProcessors should not be used on GridRoomEmpty");
    }

    @Override
    public AbstractGridRoom withStructureProcessor(StructureProcessor processor) {
        throw new IllegalStateException("withStructureProcessor should not be used on GridRoomEmpty");
    }

    @Override
    public AbstractGridRoom getCopy() {
        return new GridRoomEmpty(gridWidth, gridHeight, differentiationID)
                .setSizeHeight(northSizeScale, eastSizeScale, heightScale)
                .setOffsets(connectionOffsets)
                .setConnectionTags(connectionTags)
                .setConnections(connections)
                .doAllowRotation(allowRotation, allowUpDownConnectedRotation)
                .withWeight(weight)
                .setGenerationPriority(generationPriority)
                .setOverrideEndChance(overrideEndChance, doOverrideEndChance)
                .setSpawnRules(spawnRules)
                .setStructureProcessors(structureProcessors, structurePostProcessors)
                .setSkipCollectionProcessors(skipCollectionProcessors, skipCollectionPostProcessors);

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomEmpty otherRoom &&
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
                ListAndArrayUtils.listEquals(structurePostProcessors.list(), otherRoom.structurePostProcessors.list()) &&
                differentiationID == otherRoom.differentiationID &&
                skipCollectionPostProcessors == otherRoom.skipCollectionPostProcessors;
    }

    @Override
    public int hashCode() {
        int result = 17;
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
        result = 31 * result + structurePostProcessors.list().hashCode();
        result = 31 * result + differentiationID;
        result = 31 * result + (skipCollectionPostProcessors ? 1 : 0);
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
                forEachBlockPosInBounds(centre, roomRotation, postProcessorData.getMethod(), pos -> {
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
