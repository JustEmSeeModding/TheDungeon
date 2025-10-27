package net.emsee.thedungeon.dungeon.src.types.grid.room;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.StructureUtils;
import net.emsee.thedungeon.utils.WeightedMap;
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

/** Similar to GridRoomGroup but only uses multiple resource locations */
public class GridRoomMultiResource extends AbstractGridRoom {
    private final MultiData multiData;

    private GridRoomMultiResource(MultiData multiData, Data data) {
        super(data);
        this.multiData=multiData;
    }

    public static Builder builder(int gridWidth, int gridHeight) {
        return new Builder(gridWidth, gridHeight, 0);
    }

    public static Builder builder(int gridWidth, int gridHeight, int differentiationID) {
        return new Builder(gridWidth, gridHeight, differentiationID);
    }

    private static class MultiData {
        WeightedMap.Int<ResourceLocation> resourceLocations = new WeightedMap.Int<>();
    }


    public static class Builder extends AbstractGridRoom.Builder<GridRoomMultiResource,Builder> {
        private final MultiData multiData;

        protected Builder(int gridWidth, int gridHeight, int differentiationID) {
            super(gridWidth, gridHeight, differentiationID);
            multiData = new MultiData();
        }

        private Builder(MultiData multiData, Data data) {
            super(data);
            this.multiData = multiData;
        }

        @Override
        public GridRoomMultiResource build() {
            return new GridRoomMultiResource(multiData, data);
        }

        public Builder withResourceLocation(ResourceLocation resourceLocation, int weight) {
            if (multiData.resourceLocations.containsKey(resourceLocation))
                throw new IllegalArgumentException("MultiResourceGridRoom already contains this ResourceLocation (" + resourceLocation + ")");
            if (weight <= 0)
                throw new IllegalArgumentException("weight must be > 0");
            multiData.resourceLocations.put(resourceLocation, weight);
            return this;
        }

        public Builder withResourceLocation(String path, int weight) {
            return this.withResourceLocation(TheDungeon.MOD_ID, path, weight);
        }

        public Builder withResourceLocation(String nameSpace, String path, int weight) {
            return this.withResourceLocation(ResourceLocation.fromNamespaceAndPath(nameSpace, path), weight);
        }
    }



    public ResourceLocation getResourceLocation(Random random) {
        return multiData.resourceLocations.getRandom(random);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomMultiResource otherRoom &&
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
                ListAndArrayUtils.mapEquals(multiData.resourceLocations, otherRoom.multiData.resourceLocations) &&
                data.skipCollectionProcessors == otherRoom.data.skipCollectionProcessors &&
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
        result = 31 * result + Integer.hashCode(data.northSizeScale);
        result = 31 * result + Integer.hashCode(data.eastSizeScale);
        result = 31 * result + Integer.hashCode(data.heightScale);
        result = 31 * result + data.connectionOffsets.hashCode();
        result = 31 * result + data.connectionTags.hashCode();
        result = 31 * result + data.generationPriority;
        result = 31 * result + Double.hashCode(data.overrideEndChance);
        result = 31 * result + (data.doOverrideEndChance ? 1 : 0);
        result = 31 * result + data.spawnRules.hashCode();
        result = 31 * result + data.structureProcessors.list().hashCode();
        result = 31 * result + data.structurePostProcessors.list().hashCode();
        result = 31 * result + data.differentiationID;
        result = 31 * result + multiData.resourceLocations.hashCode();
        result = 31 * result + (data.skipCollectionProcessors ? 1 : 0);
        result = 31 * result + (data.skipCollectionPostProcessors ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MultiResourceGridRoom:" + ListAndArrayUtils.mapToString(multiData.resourceLocations);
    }

    @Override
    public void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {
        StructureTemplate template = StructureUtils.getTemplate(serverLevel, getResourceLocation(random));
        if (template == null) {
            throw new IllegalStateException(this + ": template was null");
        }
        if (centre == null) {
            throw new IllegalStateException(this + ": Placement position was null");
        }
        if (roomRotation == null) {
            throw new IllegalStateException(this + ": Placement rotation was null");
        }

        BlockPos origin = centre.subtract(new Vec3i(Math.round((getGridCellWidth()) * getEastSizeScale() / 2f) - 1, 0, Math.round((getGridCellWidth()) * getNorthSizeScale() / 2f) - 1));
        BlockPos minCorner = centre.subtract(new Vec3i(getGridCellWidth() * getMaxSizeScale(), 0, getGridCellWidth() * getMaxSizeScale()));
        BlockPos maxCorner = centre.offset(new Vec3i(getGridCellWidth() * getMaxSizeScale(), getGridCellHeight() * getHeightScale(), getGridCellWidth() * getMaxSizeScale()));
        RandomSource rand = RandomSource.create(serverLevel.dimension().location().hashCode() + Math.round(random.nextDouble() * 1000));
        BoundingBox mbb = BoundingBox.fromCorners(minCorner, maxCorner);

        StructurePlaceSettings placement = new StructurePlaceSettings()
                .setRandom(rand)
                .addProcessor(JigsawReplacementProcessor.INSTANCE)
                //.addProcessor()
                .setRotationPivot(new BlockPos(getGridCellWidth() * getEastSizeScale() / 2, 0, getGridCellWidth() * getNorthSizeScale() / 2))
                .setRotation(roomRotation)
                .setBoundingBox(mbb)
                .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);

        for (StructureProcessor processor : processors.list()) {
            if (processor instanceof PostProcessor)
                throw new IllegalStateException("Adding post processor as normal processor");
            placement.addProcessor(processor);
        }

        template.placeInWorld(serverLevel, origin, origin, placement, rand, Block.UPDATE_ALL);
    }

    @Override
    public void postProcess(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList postProcessors, Random random) {
        if (postProcessors == null || postProcessors.list().isEmpty()) return;
        final BlockPos origin = getMinCorner(centre, roomRotation);
        final StructurePlaceSettings settings = new StructurePlaceSettings();
        for (StructureProcessor processor : postProcessors.list()) {
            if (processor instanceof PostProcessor postProcessorData)
                forEachBlockPosInBounds(centre, roomRotation, postProcessorData.getMethod(),serverLevel, pos -> {
                    BlockState initialState = serverLevel.getBlockState(pos);
                    if (!postProcessorData.skipBlockForProcessing(serverLevel, pos, initialState))
                        return;
                    // Create a StructureBlockInfo for the block
                    StructureTemplate.StructureBlockInfo blockInfo = new StructureTemplate.StructureBlockInfo(
                            pos,
                            initialState,
                            null
                    );

                    blockInfo = processor.process(serverLevel, origin, pos, blockInfo, blockInfo, settings, null);

                    // Place processed block state (or fallback to initial state)
                    serverLevel.setBlockAndUpdate(pos, blockInfo != null ? blockInfo.state() : initialState);
                });
            else throw new IllegalStateException("Adding normal processor as post processor");
        }
    }
}

