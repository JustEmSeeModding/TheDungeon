package net.emsee.thedungeon.dungeon.room;

import net.emsee.thedungeon.TheDungeon;
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
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Random;

/** Similar to GridRoomGroup but only uses multiple resource locations */
public class GridRoomMultiResource extends AbstractGridRoom {
    WeightedMap.Int<ResourceLocation> resourceLocations = new WeightedMap.Int<>();

    public GridRoomMultiResource(int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight);
    }

    public GridRoomMultiResource(int gridWidth, int gridHeight, int ID) {
        super(gridWidth, gridHeight, ID);
    }

    public GridRoomMultiResource withResourceLocation(ResourceLocation resourceLocation, int weight) {
        if (resourceLocations.containsKey(resourceLocation))
            throw new IllegalStateException("MultiResourceGridRoom already contains this ResourceLocation (" + resourceLocation + ")");
        resourceLocations.put(resourceLocation, weight);
        return this;
    }

    public GridRoomMultiResource withResourceLocation(String path, int weight) {
        return this.withResourceLocation(TheDungeon.MOD_ID, path, weight);
    }

    public GridRoomMultiResource withResourceLocation(String nameSpace, String path, int weight) {
        return this.withResourceLocation(ResourceLocation.fromNamespaceAndPath(nameSpace, path), weight);
    }

    protected GridRoomMultiResource setResourceLocations(WeightedMap.Int<ResourceLocation> resourceLocations) {
        this.resourceLocations.clear();
        this.resourceLocations.putAll(resourceLocations);
        return this;
    }

    public ResourceLocation getResourceLocation(Random random) {
        return resourceLocations.getRandom(random);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomMultiResource otherRoom &&
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
                differentiationID == otherRoom.differentiationID &&
                ListAndArrayUtils.mapEquals(resourceLocations, otherRoom.resourceLocations) &&
                skipCollectionProcessors == otherRoom.skipCollectionProcessors;
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
        result = 31 * result + differentiationID;
        result = 31 * result + resourceLocations.hashCode();
        result = 31 * result + (skipCollectionProcessors ? 1 : 0);
        return result;
    }

    /**
     * Creates a copy of this.
     */
    public AbstractGridRoom getCopy() {
        return new GridRoomMultiResource(gridWidth, gridHeight, differentiationID)
                .setResourceLocations(resourceLocations)
                .setSizeHeight(northSizeScale, eastSizeScale, heightScale)
                .setOffsets(connectionOffsets)
                .setConnectionTags(connectionTags)
                .setConnections(connections)
                .doAllowRotation(allowRotation, allowUpDownConnectedRotation)
                .withWeight(weight)
                .setGenerationPriority(generationPriority)
                .setOverrideEndChance(overrideEndChance, doOverrideEndChance)
                .setSpawnRules(spawnRules)
                .setStructureProcessors(structureProcessors)
                .setSkipCollectionProcessors(skipCollectionProcessors);

    }

    @Override
    public String toString() {
        return "MultiResourceGridRoom:" + ListAndArrayUtils.mapToString(resourceLocations);
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

        BlockPos origin = centre.subtract(new Vec3i(Math.round((getGridCellWidth()) * getRotatedEastSizeScale(Rotation.NONE) / 2f) - 1, 0, Math.round((getGridCellWidth()) * getRotatedNorthSizeScale(Rotation.NONE) / 2f) - 1));
        BlockPos minCorner = centre.subtract(new Vec3i(getGridCellWidth() * getMaxSizeScale(), 0, getGridCellWidth() * getMaxSizeScale()));
        BlockPos maxCorner = centre.offset(new Vec3i(getGridCellWidth() * getMaxSizeScale(), getGridCellHeight() * getHeightScale(), getGridCellWidth() * getMaxSizeScale()));
        RandomSource rand = RandomSource.create(serverLevel.dimension().location().hashCode() + Math.round(Math.random() * 1000));
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
            placement.addProcessor(processor);
        }

        template.placeInWorld(serverLevel, origin, origin, placement, rand, Block.UPDATE_ALL);
    }
}

