package net.emsee.thedungeon.dungeon.src.types.grid.room;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Random;

public final class GridRoomBasic extends AbstractGridRoom{
    private final BasicData basicData;

    private GridRoomBasic(BasicData basicData, Data data) {
        super(data);
        this.basicData = basicData;
    }

    private static class BasicData {
        protected final ResourceLocation resourceLocation;
        BasicData(ResourceLocation resourceLocation) {
            this.resourceLocation = resourceLocation;
        }
    }

    public static Builder builder(String path, int gridWidth, int gridHeight) {
        return builder(path, gridWidth, gridHeight, 0);
    }

    public static Builder builder(String path, int gridWidth, int gridHeight, int differentiationID) {
        return new Builder(path, gridWidth, gridHeight, differentiationID);
    }

    public static Builder builder(ResourceLocation resourceLocation, int gridWidth, int gridHeight) {
        return builder(resourceLocation, gridWidth, gridHeight, 0);
    }

    public static Builder builder(ResourceLocation resourceLocation, int gridWidth, int gridHeight, int differentiationID) {
        return new Builder(resourceLocation, gridWidth, gridHeight, differentiationID);
    }

    @Override
    public Builder edit() {
        return new Builder(basicData, data);
    }

    public static class Builder extends AbstractGridRoom.Builder<GridRoomBasic> {
        final BasicData basicData;

        protected Builder(String path, int gridWidth, int gridHeight, int differentiationID) {
            this(TheDungeon.defaultResourceLocation(path), gridWidth, gridHeight, differentiationID);
        }

        protected Builder(ResourceLocation resourceLocation, int gridWidth, int gridHeight, int differentiationID) {
            super(gridWidth, gridHeight, differentiationID);
            basicData=new BasicData(resourceLocation);
        }

        private Builder(BasicData basicData, Data data) {
            super(data);
            this.basicData=basicData;
        }

        @Override
        public GridRoomBasic build() {
            return new GridRoomBasic(basicData, data);
        }
    }

    public ResourceLocation getResourceLocation() {
        return basicData.resourceLocation;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomBasic otherRoom &&
                basicData.resourceLocation == otherRoom.basicData.resourceLocation &&
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
                ListAndArrayUtils.listEquals(data.structureProcessors.list(), otherRoom.data.structureProcessors.list()) &&
                ListAndArrayUtils.listEquals(data.structurePostProcessors.list(), otherRoom.data.structurePostProcessors.list()) &&
                data.differentiationID == otherRoom.data.differentiationID &&
                data.skipCollectionProcessors == otherRoom.data.skipCollectionProcessors &&
                data.skipCollectionPostProcessors == otherRoom.data.skipCollectionPostProcessors;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (basicData.resourceLocation != null ? basicData.resourceLocation.hashCode() : 0);
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
        result = 31 * result + (data.skipCollectionProcessors ? 1 : 0);
        result = 31 * result + (data.skipCollectionPostProcessors ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GridRoomBasic("+basicData.resourceLocation.getPath()+")";
    }

    @Override
    public void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {
        StructureTemplate template = StructureUtils.getTemplate(serverLevel, getResourceLocation());
        if (template == null)
            throw new IllegalStateException(this + ": template was null");
        if (centre == null)
            throw new IllegalStateException(this + ": Placement position was null");
        if (roomRotation == null)
            throw new IllegalStateException(this + ": Placement rotation was null");


        BlockPos origin = centre.subtract(new Vec3i(Math.round((getGridCellWidth()) * getRotatedEastSizeScale(Rotation.NONE) / 2f) - 1, 0, Math.round((getGridCellWidth()) * getRotatedNorthSizeScale(Rotation.NONE) / 2f) - 1));
        BlockPos minCorner = centre.subtract(new Vec3i(getGridCellWidth() * getMaxSizeScale(), 0, getGridCellWidth() * getMaxSizeScale()));
        BlockPos maxCorner = centre.offset(new Vec3i(getGridCellWidth() * getMaxSizeScale(), getGridCellHeight() * getHeightScale(), getGridCellWidth() * getMaxSizeScale()));
        RandomSource rand = RandomSource.create(serverLevel.dimension().location().hashCode() + Math.round(random.nextDouble() * 1000));
        BoundingBox mbb = BoundingBox.fromCorners(minCorner, maxCorner);

        StructurePlaceSettings placement = new StructurePlaceSettings()
                .setRandom(rand)
                .addProcessor(JigsawReplacementProcessor.INSTANCE)
                //.addProcessor()
                .setRotationPivot(new BlockPos(getGridCellWidth() * getRotatedEastSizeScale(Rotation.NONE) / 2, 0, getGridCellWidth() * getRotatedNorthSizeScale(Rotation.NONE) / 2))
                .setRotation(roomRotation)
                .setBoundingBox(mbb)
                .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);

        //new RuleProcessor(ImmutableList.of(new ProcessorRule(new RandomBlockStateMatchTest(Blocks.AIR.defaultBlockState(),.1f), AlwaysTrueTest.INSTANCE, ModBlocks.DUNGEON_MOD_SPAWNER.get().defaultBlockState())))

        for (StructureProcessor processor : processors.list()) {
            if (processor instanceof PostProcessor)
                throw new IllegalStateException("Adding post processor as normal processor");
            placement.addProcessor(processor);
        }

        template.placeInWorld(serverLevel, origin, origin, placement, rand, Block.UPDATE_ALL);
    }

    @Override
    public void postProcess(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList postProcessors, Random random) {
        for (StructureProcessor processor : postProcessors.list()) {
            if (processor instanceof PostProcessor postProcessorData)
                forEachBlockPosInBounds(centre, roomRotation, postProcessorData.getMethod(),serverLevel, pos -> {
                    BlockState initialState = serverLevel.getBlockState(pos);
                    if (postProcessorData.skipBlockForProcessing(serverLevel, pos, initialState))
                        return;
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
