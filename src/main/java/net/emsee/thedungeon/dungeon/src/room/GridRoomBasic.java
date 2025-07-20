package net.emsee.thedungeon.dungeon.src.room;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Random;

public class GridRoomBasic extends AbstractGridRoom{
    protected final ResourceLocation resourceLocation;
    protected ResourceKey<Biome> biome = null;

    public GridRoomBasic(String path, int gridWidth, int gridHeight) {
        this(TheDungeon.defaultResourceLocation(path), gridWidth, gridHeight);
    }

    public GridRoomBasic(String path,int gridWidth, int gridHeight, int ID) {
        this(TheDungeon.defaultResourceLocation(path), gridWidth, gridHeight, ID);
    }

    public GridRoomBasic(ResourceLocation resourceLocation, int gridWidth, int gridHeight) {
        super(gridWidth, gridHeight);
        this.resourceLocation = resourceLocation;
    }

    public GridRoomBasic(ResourceLocation resourceLocation,int gridWidth, int gridHeight, int ID) {
        super(gridWidth, gridHeight, ID);
        this.resourceLocation = resourceLocation;
    }

    public GridRoomBasic setBiome(ResourceKey<Biome> biome) {
        this.biome=biome;
        return this;
    }

    @Override
    public AbstractGridRoom getCopy() {
        return new GridRoomBasic(resourceLocation ,gridWidth, gridHeight, differentiationID)
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

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GridRoomBasic otherRoom &&
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
                differentiationID == otherRoom.differentiationID &&
                skipCollectionProcessors == otherRoom.skipCollectionProcessors;
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
        result = 31 * result + (skipCollectionProcessors ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GridRoomBasic("+resourceLocation.getPath()+")";
    }

    @Override
    public void placeFeature(ServerLevel serverLevel, BlockPos centre, Rotation roomRotation, StructureProcessorList processors, Random random) {
        StructureTemplate template = StructureUtils.getTemplate(serverLevel, getResourceLocation());
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
            placement.addProcessor(processor);
        }

        template.placeInWorld(serverLevel, origin, origin, placement, rand, Block.UPDATE_ALL);
    }
}
