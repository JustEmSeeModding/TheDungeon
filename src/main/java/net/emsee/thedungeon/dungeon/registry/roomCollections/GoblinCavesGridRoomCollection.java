package net.emsee.thedungeon.dungeon.registry.roomCollections;

import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.types.grid.room.*;
import net.emsee.thedungeon.dungeon.src.types.grid.roomCollection.GridRoomCollection;
import net.emsee.thedungeon.dungeon.src.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules.SpawnInRoom;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.emsee.thedungeon.structureProcessor.WaterloggingProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.*;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post.CrystalCaveBuddingPostProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post.OvergrownPostProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post.SnowLayerPostProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.CrystalCaveClusterProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.DirtClusterProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.MagmaHollowClusterProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.StoneCaveDioriteAndGraniteProcessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

import java.util.function.Consumer;

public final class GoblinCavesGridRoomCollection extends GridRoomCollection {
    private final static String STONE_TAG = "stone_caves";
    private final static String OVERGROWN_TAG = "overgrown_caves";
    private final static String ICE_TAG = "ice_caves";

    private final static String DEEP_TAG = "deep_caves";
    private final static String GILDED_TAG = "gilded_caves";
    private final static String FUNGAL_TAG = "fungal_caves";
    private final static String WATERLOGGED = "waterlogged";

    private final static String CRYSTAL_TAG = "crystal_caves";
    private final static String MAGMA_TAG = "magma_caves";

    private final static Consumer<HobGoblinEntity> noForgeWorker = e -> {while (e.getVariant()==HobGoblinEntity.Variant.FORGER) {e.setRandomVariant();}};

    public GoblinCavesGridRoomCollection() {
        super(11, 11);
        this

                .addRequiredRoomsOf(20, 50, spawn_rooms().build().getList())
                .addRequiredRoomsOf(5, dens().build().getList())

                // the magma forge
                .addRequiredRoom(1,1, GridRoomBasic.builder("goblin_caves/magma/forge",11,11)
                        .setSizeHeight(5,5,3)
                        .withWeight(5)
                        .horizontalConnections()
                        .setHorizontalConnectionOffset(Connection.NORTH,0,2)
                        .setHorizontalConnectionOffset(Connection.EAST,0,2)
                        .setHorizontalConnectionOffset(Connection.SOUTH,0,2)
                        .setHorizontalConnectionOffset(Connection.WEST,0,2)
                        .setAllConnectionTags(MAGMA_TAG)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(MagmaHollowClusterProcessor.INSTANCE)
                        .withStructureProcessor(StoneToMagmaCaveProcessor.INSTANCE)
                        .setBiome(DungeonBiome.magma_cave)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.HOB_GOBLIN, e-> e.setVariant(HobGoblinEntity.Variant.FORGER), 2, 5, 1))
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.HOB_GOBLIN, noForgeWorker,0, 6,1))
                        .build()
                )

                // stone to gilded
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, STONE_TAG)
                        .setConnectionTag(Connection.SOUTH, GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //deep to crystal
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_deep", 11, 11)
                        .setHeight(2)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, CRYSTAL_TAG)
                        .setConnectionTag(Connection.SOUTH, DEEP_TAG)
                        .setHorizontalConnectionOffset(Connection.NORTH, 0,1)
                        .doAllowRotation()
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(StoneToCrystalBaseProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveClusterProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveProcessor.INSTANCE)
                        .withStructurePostProcessor(CrystalCaveBuddingPostProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //stone to deep
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_deep", 11, 11)
                        .setHeight(2)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, STONE_TAG)
                        .setConnectionTag(Connection.SOUTH, DEEP_TAG)
                        .setHorizontalConnectionOffset(Connection.NORTH, 0,1)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //stone to waterlogged
                .addRequiredRoom(1, 2, GridRoomBasic.builder("goblin_caves/convert/stone_waterlogged", 11, 11)
                        .setHeight(2)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, STONE_TAG)
                        .setConnectionTag(Connection.SOUTH, WATERLOGGED)
                        .setHorizontalConnectionOffset(Connection.NORTH, 0,1)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //deep to gilded
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepslateBaseProcessor.INSTANCE)
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //ice to deep
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, ICE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(BlackstoneToDeepslateProcessor.INSTANCE)
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //stone to overgrown
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_overgrown", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, STONE_TAG)
                        .setConnectionTag(Connection.SOUTH, OVERGROWN_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //overgrown to fungal
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/fungal_overgrown", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, FUNGAL_TAG)
                        .setConnectionTag(Connection.SOUTH, OVERGROWN_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE)
                        .withStructureProcessor(FungalVegetationOnlyProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //deep to crystal
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, CRYSTAL_TAG)
                        .setConnectionTag(Connection.SOUTH, DEEP_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToCrystalBaseProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveProcessor.INSTANCE)
                        .withStructurePostProcessor(CrystalCaveBuddingPostProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToDeepslateProcessor.INSTANCE)
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                //gilded to magma
                .addRequiredRoom(1, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, GILDED_TAG)
                        .setConnectionTag(Connection.SOUTH, MAGMA_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(MagmaHollowClusterProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                .addRooms(stone_biome().build())
                .addRooms(gilded_biome().build())
                .addRooms(deep_biome().build())
                .addRequiredRoomsOf(5, 30, waterlogged_biome().build())
                .addRooms(ice_biome().build())
                .addRooms(overgrown_biome().build())
                .addRooms(fungal_biome().build())
                .addRooms(crystal_biome().build())
                .addRooms(magma_biome().build())

                .addTagRule(new WallFailRule(STONE_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(GILDED_TAG, 11, 11, 0, false, Blocks.BLACKSTONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(DEEP_TAG, 11, 11, 0, false, Blocks.DEEPSLATE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(WATERLOGGED, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(ICE_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(OVERGROWN_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(DirtClusterProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(FUNGAL_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(DirtClusterProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(FungalCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(CRYSTAL_TAG, 11, 11, 0, false, Blocks.CALCITE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(CrystalCaveClusterProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveBuddingPostProcessor.INSTANCE))
                .addTagRule(new WallFailRule(MAGMA_TAG, 11, 11, 0, false, Blocks.BLACKSTONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(MagmaHollowClusterProcessor.INSTANCE)
                        .withStructureProcessor(MagmaCaveProcessor.INSTANCE))
        ;
    }

    private static GridRoomList.Builder spawn_rooms() {
        return GridRoomList.builder().addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(3)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(ICE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE)
                        .withStructurePostProcessor(SnowLayerPostProcessor.INSTANCE)
                        .setBiome(DungeonBiome.ice_cave))
                .addRoom(GridRoomBasic.builder("goblin_caves/overgrown/spawn", 11, 11)
                        .withWeight(3)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(OVERGROWN_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(DirtClusterProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE));
    }

    private static GridRoomList.Builder dens() {
        return GridRoomList.builder()
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/den", 11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 2, 5, 1)))
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/den", 11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.SHADOW_GOBLIN, 2, 5, 1)))
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/den", 11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(DEEP_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepslateBaseProcessor.INSTANCE)
                        .withStructureProcessor(DeepCaveProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 2, 5, 1)));
    }

    private static GridRoomList.Builder stone_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(STONE_TAG)))
                .addRoom(l_stone_vein().withWeight(5).withStructureProcessor(StoneCaveOreProcessor.INSTANCE).setAllConnectionTags(STONE_TAG))

                .applyToAll(room -> room.addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 1, 2, .15f)));
    }

    private static GridRoomList.Builder gilded_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(GILDED_TAG))
                        .addRoom(l_blackstone_vein().withWeight(5).withStructureProcessor(GildedCaveOreProcessor.INSTANCE).setAllConnectionTags(GILDED_TAG)))

                .applyToAll(room -> room.addMobSpawnRule(new SpawnInRoom<>(ModEntities.SHADOW_GOBLIN, 1, 2, .12f)));
    }

    private static GridRoomList.Builder deep_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToDeepslateBaseProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(DeepCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(DEEP_TAG)))

                .applyToAll(room -> room.addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 1, 2, .15f)));
    }

    private static GridRoomList.Builder ice_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructurePostProcessor(SnowLayerPostProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(ICE_TAG))
                );
    }

    private static GridRoomList.Builder overgrown_biome() {
        return GridRoomList.builder()
                .addRoom(i_overgrown().withWeight(60).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).withStructurePostProcessor(OvergrownPostProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG))
                .addRoom(l_overgrown().withWeight(50).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).withStructurePostProcessor(OvergrownPostProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG))
                .addRoom(t_overgrown().withWeight(30).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).withStructurePostProcessor(OvergrownPostProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG));
    }

    private static GridRoomList.Builder waterlogged_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(WaterloggingProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(WATERLOGGED)));
    }

    private static GridRoomList.Builder fungal_biome() {
        return GridRoomList.builder()
                .addRoom(i_fungal().withWeight(60).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(FungalCaveProcessor.INSTANCE).setAllConnectionTags(FUNGAL_TAG))
                .addRoom(l_fungal().withWeight(50).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(FungalCaveProcessor.INSTANCE).setAllConnectionTags(FUNGAL_TAG));
        //.addRoom(t_overgrown().withWeight(30).withStructureProcessor(StoneCaveOreProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG));
    }

    private static GridRoomList.Builder crystal_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToCrystalBaseProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(CrystalCaveClusterProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(CrystalCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructurePostProcessor(CrystalCaveBuddingPostProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(CRYSTAL_TAG)));
    }

    private static GridRoomList.Builder magma_biome() {
        return GridRoomList.builder()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(MagmaHollowClusterProcessor.INSTANCE))
                        .applyToAll(room -> room.withStructureProcessor(StoneToMagmaCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setBiome(DungeonBiome.magma_cave))
                        .applyToAll(room -> room.setAllConnectionTags(MAGMA_TAG)));
    }


    private static AbstractGridRoom.Builder<?,?> i_overgrown() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/overgrown/i/one", 10)
                .withResourceLocation("goblin_caves/overgrown/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> l_overgrown() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/overgrown/l/one", 10)
                .withResourceLocation("goblin_caves/overgrown/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> t_overgrown() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/overgrown/t/one", 10)
                .withResourceLocation("goblin_caves/overgrown/t/two", 10)
                .horizontalConnections(1, 0, 1, 1)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> i_fungal() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/fungal/i/one", 10)
                .withResourceLocation("goblin_caves/fungal/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> l_fungal() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/fungal/l/one", 10)
                .withResourceLocation("goblin_caves/fungal/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static GridRoomList.Builder unassigned_generic_path_caves() {
        return GridRoomList.builder()
                .addRoom(i_stone().withWeight(60))
                .addRoom(l_stone().withWeight(50))
                .addRoom(
                        GridRoomMultiResource.builder(11, 11)
                                .withResourceLocation("goblin_caves/stone/t_large/one", 5)
                                .withResourceLocation("goblin_caves/stone/t_large/crevice", 2)
                                .withWeight(8)
                                .setSizeHeight(3, 3, 1)
                                .horizontalConnections(1, 1, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        GridRoomMultiResource.builder(11, 11)
                                .withResourceLocation("goblin_caves/stone/t_large/elevated_one", 2)
                                .withResourceLocation("goblin_caves/stone/t_large/elevated_cliff", 1)
                                .withWeight(8)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections(1, 1, 0, 1)
                                .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        GridRoomBasic.builder("goblin_caves/stone/x_large/elevated_one", 11, 11)
                                .withWeight(3)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        GridRoomBasic.builder("goblin_caves/stone/x_large/elevated_two", 11, 11)
                                .withWeight(3).setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        GridRoomBasic.builder("goblin_caves/stone/x_large/bridge", 11, 11)
                                .withWeight(2)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation())
                .addRoom(GridRoomBasic.builder("goblin_caves/convert/stone_deep", 11, 11)
                        .setHeight(2)
                        .withWeight(8)
                        .horizontalConnections(1, 0, 1, 0)
                        .setHorizontalConnectionOffset(Connection.NORTH, 0,1)
                        .doAllowRotation()
                        .withStructureProcessor(DeepslateToStoneBaseProcessor.INSTANCE));
    }

    private static AbstractGridRoom.Builder<?,?> i_stone() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/stone/i/one", 10)
                .withResourceLocation("goblin_caves/stone/i/two", 11)
                .withResourceLocation("goblin_caves/stone/i/three", 11)
                .withResourceLocation("goblin_caves/stone/i/pillar", 1)
                .withResourceLocation("goblin_caves/stone/i/pillar_broken", 2)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> l_stone() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/stone/l/one", 10)
                .withResourceLocation("goblin_caves/stone/l/two", 10)
                .withResourceLocation("goblin_caves/stone/l/three", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> l_stone_vein() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/stone/l/iron_vein", 8)
                .withResourceLocation("goblin_caves/stone/l/copper_vein", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?,?> l_blackstone_vein() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/blackstone/l/gold_vein", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }
}
