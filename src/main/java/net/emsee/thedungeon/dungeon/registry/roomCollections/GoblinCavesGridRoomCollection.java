package net.emsee.thedungeon.dungeon.registry.roomCollections;

import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.types.roomCollection.GridRoomCollection;
import net.emsee.thedungeon.dungeon.src.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules.SpawnInRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.AbstractGridRoom;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomBasic;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomList;
import net.emsee.thedungeon.dungeon.src.types.grid.room.GridRoomMultiResource;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.*;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post.CrystalCaveBuddingProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.CrystalCaveClusterProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.DirtClusterProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.clusters.StoneCaveDioriteAndGraniteProcessor;
import net.minecraft.world.level.block.Blocks;

public final class GoblinCavesGridRoomCollection extends GridRoomCollection {
    private final static String STONE_TAG = "stone_caves";
    private final static String GILDED_TAG = "gilded_caves";
    private final static String DEEP_TAG = "deep_caves";
    private final static String ICE_TAG = "ice_caves";
    private final static String OVERGROWN_TAG = "overgrown_caves";
    private final static String FUNGAL_TAG = "fungal_caves";
    private final static String CRYSTAL_TAG = "crystal_caves";


    public GoblinCavesGridRoomCollection() {
        super(11, 11);
        this

                .addRequiredRoomsOf(25, 50, spawn_rooms())
                .addRequiredRoomsOf(5, dens())

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
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, ICE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())
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
                .addRequiredRoom(0, 2, GridRoomBasic.builder("goblin_caves/convert/stone_blackstone", 11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, CRYSTAL_TAG)
                        .setConnectionTag(Connection.SOUTH, STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToCrystalBaseProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveProcessor.INSTANCE)
                        .withStructurePostProcessor(CrystalCaveBuddingProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1).build())

                .addRooms(stone_biome())
                .addRooms(gilded_biome())
                .addRooms(deep_biome())
                .addRooms(ice_biome())
                .addRooms(overgrown_biome())
                .addRooms(fungal_biome())
                .addRooms(crystal_biome())
                .addTagRule(new WallFailRule(STONE_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(GILDED_TAG, 11, 11, 0, false, Blocks.BLACKSTONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(DEEP_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE))
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
                        .withStructureProcessor(CrystalCaveBuddingProcessor.INSTANCE))
        ;
    }

    private static GridRoomList spawn_rooms() {
        return new GridRoomList().addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(3)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(3)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(3)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(DEEP_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(ICE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/overgrown/spawn", 11, 11)
                        .withWeight(3)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(OVERGROWN_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(DirtClusterProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/fungal/spawn", 11, 11)
                        .withWeight(2)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(FUNGAL_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(DirtClusterProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(FungalCaveProcessor.INSTANCE).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/spawn", 11, 11)
                        .withWeight(2)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(CRYSTAL_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToCrystalBaseProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveClusterProcessor.INSTANCE)
                        .withStructureProcessor(CrystalCaveProcessor.INSTANCE)
                        .withStructurePostProcessor(CrystalCaveBuddingProcessor.INSTANCE).build());
    }

    private static GridRoomList dens() {
        return new GridRoomList()
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/den", 11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 2, 5, 1)).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/den", 11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.SHADOW_GOBLIN, 2, 5, 1)).build())
                .addRoom(GridRoomBasic.builder("goblin_caves/stone/den", 11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(DEEP_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 2, 5, 1)).build());
    }

    private static GridRoomList stone_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.edit().withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().withStructureProcessor(StoneCaveOreProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().setAllConnectionTags(STONE_TAG).build()))
                .addRoom(l_stone_vein().withWeight(5).withStructureProcessor(StoneCaveOreProcessor.INSTANCE).setAllConnectionTags(STONE_TAG).build())

                .applyToAll(room -> room.edit().addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 1, 2, .15f)).build());
    }

    private static GridRoomList gilded_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.edit().withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().setAllConnectionTags(GILDED_TAG).build())
                        .addRoom(l_blackstone_vein().withWeight(5).withStructureProcessor(GildedCaveOreProcessor.INSTANCE).setAllConnectionTags(GILDED_TAG).build()))

                .applyToAll(room -> room.edit().addMobSpawnRule(new SpawnInRoom<>(ModEntities.SHADOW_GOBLIN, 1, 2, .12f)).build());
    }

    private static GridRoomList deep_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.edit().withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().setAllConnectionTags(DEEP_TAG).build()))

                .applyToAll(room -> room.edit().addMobSpawnRule(new SpawnInRoom<>(ModEntities.CAVE_GOBLIN, 1, 2, .15f)).build());
    }

    private static GridRoomList ice_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.edit().withStructureProcessor(StoneToIceCaveProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().setAllConnectionTags(ICE_TAG).build())
                );
    }

    private static GridRoomList overgrown_biome() {
        return new GridRoomList()
                .addRoom(i_overgrown().withWeight(60).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG).build())
                .addRoom(l_overgrown().withWeight(50).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG).build())
                .addRoom(t_overgrown().withWeight(30).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG).build());
    }

    private static GridRoomList fungal_biome() {
        return new GridRoomList()
                .addRoom(i_fungal().withWeight(60).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(FungalCaveProcessor.INSTANCE).setAllConnectionTags(FUNGAL_TAG).build())
                .addRoom(l_fungal().withWeight(50).withStructureProcessor(DirtClusterProcessor.INSTANCE).withStructureProcessor(StoneCaveDioriteAndGraniteProcessor.INSTANCE).withStructureProcessor(FungalCaveProcessor.INSTANCE).setAllConnectionTags(FUNGAL_TAG).build());
        //.addRoom(t_overgrown().withWeight(30).withStructureProcessor(StoneCaveOreProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG));
    }

    private static GridRoomList crystal_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.edit().withStructureProcessor(StoneToCrystalBaseProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().withStructureProcessor(CrystalCaveClusterProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().withStructureProcessor(CrystalCaveProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().withStructurePostProcessor(CrystalCaveBuddingProcessor.INSTANCE).build())
                        .applyToAll(room -> room.edit().setAllConnectionTags(CRYSTAL_TAG).build()));
    }


    private static AbstractGridRoom.Builder<?> i_overgrown() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/overgrown/i/one", 10)
                .withResourceLocation("goblin_caves/overgrown/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> l_overgrown() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/overgrown/l/one", 10)
                .withResourceLocation("goblin_caves/overgrown/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> t_overgrown() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/overgrown/t/one", 10)
                .withResourceLocation("goblin_caves/overgrown/t/two", 10)
                .horizontalConnections(1, 0, 1, 1)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> i_fungal() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/fungal/i/one", 10)
                .withResourceLocation("goblin_caves/fungal/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> l_fungal() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/fungal/l/one", 10)
                .withResourceLocation("goblin_caves/fungal/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static GridRoomList unassigned_generic_path_caves() {
        return new GridRoomList()
                .addRoom(i_stone().withWeight(60).build())
                .addRoom(l_stone().withWeight(50).build())
                .addRoom(
                        GridRoomMultiResource.builder(11, 11)
                                .withResourceLocation("goblin_caves/stone/t_large/one", 5)
                                .withResourceLocation("goblin_caves/stone/t_large/crevice", 2)
                                .withWeight(8)
                                .setSizeHeight(3, 3, 1)
                                .horizontalConnections(1, 1, 0, 1)
                                .doAllowRotation().build())
                .addRoom(
                        GridRoomMultiResource.builder(11, 11)
                                .withResourceLocation("goblin_caves/stone/t_large/elevated_one", 2)
                                .withResourceLocation("goblin_caves/stone/t_large/elevated_cliff", 1)
                                .withWeight(8)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections(1, 1, 0, 1)
                                .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation().build())
                .addRoom(
                        GridRoomBasic.builder("goblin_caves/stone/x_large/elevated_one", 11, 11)
                                .withWeight(3)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                                .doAllowRotation().build())
                .addRoom(// TODO has an issue with some misplaced blocks
                        GridRoomBasic.builder("goblin_caves/stone/x_large/elevated_two", 11, 11)
                                .withWeight(3).setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation().build())
                .addRoom(
                        GridRoomBasic.builder("goblin_caves/stone/x_large/bridge", 11, 11)
                                .withWeight(2)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation().build());
    }

    private static AbstractGridRoom.Builder<?> i_stone() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/stone/i/one", 10)
                .withResourceLocation("goblin_caves/stone/i/two", 11)
                .withResourceLocation("goblin_caves/stone/i/three", 11)
                .withResourceLocation("goblin_caves/stone/i/pillar", 1)
                .withResourceLocation("goblin_caves/stone/i/pillar_broken", 2)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> l_stone() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/stone/l/one", 10)
                .withResourceLocation("goblin_caves/stone/l/two", 10)
                .withResourceLocation("goblin_caves/stone/l/three", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> l_stone_vein() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/stone/l/iron_vein", 8)
                .withResourceLocation("goblin_caves/stone/l/copper_vein", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom.Builder<?> l_blackstone_vein() {
        return GridRoomMultiResource.builder(11, 11)
                .withResourceLocation("goblin_caves/blackstone/l/gold_vein", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }
}
