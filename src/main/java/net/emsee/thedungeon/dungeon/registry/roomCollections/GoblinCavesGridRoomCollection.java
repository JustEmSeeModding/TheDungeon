package net.emsee.thedungeon.dungeon.registry.roomCollections;

import net.emsee.thedungeon.dungeon.src.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.src.mobSpawnRules.rules.SpawnInBox;
import net.emsee.thedungeon.dungeon.src.room.*;
import net.emsee.thedungeon.dungeon.src.Connection;
import net.emsee.thedungeon.dungeon.src.GridRoomCollection;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.structureProcessor.goblinCaves.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public final class GoblinCavesGridRoomCollection extends GridRoomCollection {
    private final static String STONE_TAG = "stone_caves";
    private final static String GILDED_TAG = "gilded_caves";
    private final static String DEEP_TAG = "deep_caves";
    private final static String ICE_TAG = "ice_caves";
    private final static String OVERGROWN_TAG = "overgrown_caves";
    private final static String FUNGAL_TAG = "fungal_caves";
    
    
    public GoblinCavesGridRoomCollection() {
        super(11, 11);
        this
                .addRequiredRoomsOf(15, 50, spawn_rooms())
                .addRequiredRoomsOf(5, dens())

                .addRequiredRoom(0, 2, new GridRoomBasic("goblin_caves/convert/stone_blackstone",11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, STONE_TAG)
                        .setConnectionTag(Connection.SOUTH, GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 2, new GridRoomBasic("goblin_caves/convert/stone_blackstone",11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 2, new GridRoomBasic("goblin_caves/convert/stone_blackstone",11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 2, new GridRoomBasic("goblin_caves/convert/stone_blackstone",11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, DEEP_TAG)
                        .setConnectionTag(Connection.SOUTH, ICE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToPlainStoneProcessor.INSTANCE)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 2, new GridRoomBasic("goblin_caves/convert/stone_overgrown",11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, STONE_TAG)
                        .setConnectionTag(Connection.SOUTH, OVERGROWN_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 2, new GridRoomBasic("goblin_caves/convert/fungal_overgrown",11, 11)
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, FUNGAL_TAG)
                        .setConnectionTag(Connection.SOUTH, OVERGROWN_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE)
                        .withStructureProcessor(FungalVegetationOnlyProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))

                .addRooms(stone_biome())
                .addRooms(gilded_biome())
                .addRooms(deep_biome())
                .addRooms(ice_biome())
                .addRooms(overgrown_biome())
                .addRooms(fungal_biome())

                .addTagRule(new WallFailRule(STONE_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(GILDED_TAG, 11, 11, 0, false, Blocks.BLACKSTONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule(DEEP_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(ICE_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(OVERGROWN_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule(FUNGAL_TAG, 11, 11, 0, false, Blocks.STONE::defaultBlockState, 11 * 11)
                        .withStructureProcessor(FungalCaveProcessor.INSTANCE))
        ;
    }
    private static GridRoomList spawn_rooms() {
        return new GridRoomList().addRoom(new GridRoomBasic("goblin_caves/stone/spawn",11, 11)
                                .withWeight(1)
                                .addConnection(Connection.NORTH)
                                .setAllConnectionTags(STONE_TAG)
                                .doAllowRotation()
                                .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                        .addRoom(new GridRoomBasic("goblin_caves/stone/spawn",11, 11)
                                .withWeight(1)
                                .addConnection(Connection.NORTH)
                                .setAllConnectionTags(GILDED_TAG)
                                .doAllowRotation()
                                .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                        .addRoom(new GridRoomBasic("goblin_caves/stone/spawn",11, 11)
                                .withWeight(1)
                                .addConnection(Connection.NORTH)
                                .setAllConnectionTags(DEEP_TAG)
                                .doAllowRotation()
                                .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE))
                        .addRoom(new GridRoomBasic("goblin_caves/stone/spawn",11, 11)
                                .withWeight(1)
                                .addConnection(Connection.NORTH)
                                .setAllConnectionTags(ICE_TAG)
                                .doAllowRotation()
                                .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                        .addRoom(new GridRoomBasic("goblin_caves/overgrown/spawn",11, 11)
                                .withWeight(1)
                                .addConnection(Connection.NORTH)
                                .setAllConnectionTags(OVERGROWN_TAG)
                                .doAllowRotation()
                                .withStructureProcessor(OvergrownCaveProcessor.INSTANCE))
                .addRoom(new GridRoomBasic("goblin_caves/fungal/spawn",11, 11)
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags(FUNGAL_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(FungalCaveProcessor.INSTANCE));
    }

    private static GridRoomList dens() {
        return new GridRoomList()
                .addRoom(new GridRoomBasic("goblin_caves/stone/den",11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(STONE_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInBox<>(ModEntities.CAVE_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)))
                .addRoom(new GridRoomBasic("goblin_caves/stone/den",11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(GILDED_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInBox<>(ModEntities.SHADOW_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)))
                .addRoom(new GridRoomBasic("goblin_caves/stone/den",11, 11)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags(DEEP_TAG)
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInBox<>(ModEntities.SHADOW_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)));
    }

    private static GridRoomList stone_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(STONE_TAG)))
                .addRoom(l_stone_vein().withWeight(5).withStructureProcessor(StoneCaveOreProcessor.INSTANCE).setAllConnectionTags(STONE_TAG));
    }
    private static GridRoomList gilded_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(GILDED_TAG))
                .addRoom(l_blackstone_vein().withWeight(5).withStructureProcessor(GildedCaveOreProcessor.INSTANCE).setAllConnectionTags(GILDED_TAG)));
    }
    private static GridRoomList deep_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToDeepCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(DEEP_TAG))
                );
    }
    private static GridRoomList ice_biome() {
        return new GridRoomList()
                .addRooms(unassigned_generic_path_caves()
                        .applyToAll(room -> room.withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags(ICE_TAG))
                );
    }

    private static GridRoomList overgrown_biome() {
        return new GridRoomList()
                .addRoom(i_overgrown().withWeight(60).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG))
                .addRoom(l_overgrown().withWeight(50).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG))
                .addRoom(t_overgrown().withWeight(30).withStructureProcessor(OvergrownCaveProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG));
    }

    private static GridRoomList fungal_biome() {
        return new GridRoomList()
                .addRoom(i_fungal().withWeight(60).withStructureProcessor(FungalCaveProcessor.INSTANCE).setAllConnectionTags(FUNGAL_TAG))
                .addRoom(l_fungal().withWeight(50).withStructureProcessor(FungalCaveProcessor.INSTANCE).setAllConnectionTags(FUNGAL_TAG));
                //.addRoom(t_overgrown().withWeight(30).withStructureProcessor(StoneCaveOreProcessor.INSTANCE).setAllConnectionTags(OVERGROWN_TAG));
    }

    private static AbstractGridRoom i_overgrown() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/overgrown/i/one", 10)
                .withResourceLocation("goblin_caves/overgrown/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }
    private static AbstractGridRoom l_overgrown() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/overgrown/l/one", 10)
                .withResourceLocation("goblin_caves/overgrown/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }
    private static AbstractGridRoom t_overgrown() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/overgrown/t/one", 10)
                .withResourceLocation("goblin_caves/overgrown/t/two", 10)
                .horizontalConnections(1, 0, 1, 1)
                .doAllowRotation();
    }

    private static AbstractGridRoom i_fungal() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/fungal/i/one", 10)
                .withResourceLocation("goblin_caves/fungal/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }
    private static AbstractGridRoom l_fungal() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/fungal/l/one", 10)
                .withResourceLocation("goblin_caves/fungal/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static GridRoomList unassigned_generic_path_caves() {
        return new GridRoomList()
                .addRoom(i_stone().withWeight(60))
                .addRoom(l_stone().withWeight(50))
                .addRoom(
                        new GridRoomMultiResource(11, 11)
                                .withResourceLocation("goblin_caves/stone/t_large/one", 5)
                                .withResourceLocation("goblin_caves/stone/t_large/crevice", 2)
                                .withWeight(8)
                                .setSizeHeight(3, 3, 1)
                                .horizontalConnections(1, 1, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        new GridRoomMultiResource(11, 11)
                                .withResourceLocation("goblin_caves/stone/t_large/elevated_one", 2)
                                .withResourceLocation("goblin_caves/stone/t_large/elevated_cliff", 1)
                                .withWeight(8)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections(1, 1, 0, 1)
                                .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        new GridRoomBasic("goblin_caves/stone/x_large/elevated_one",11, 11)
                                .withWeight(3)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                                .doAllowRotation())
                .addRoom(// TODO has an issue with some misplaced blocks
                        new GridRoomBasic("goblin_caves/stone/x_large/elevated_two",11, 11)
                                .withWeight(3).setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation())
                .addRoom(
                        new GridRoomBasic("goblin_caves/stone/x_large/bridge",11, 11)
                                .withWeight(2)
                                .setSizeHeight(3, 3, 2)
                                .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                                .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                                .doAllowRotation()
                );
    }
    private static AbstractGridRoom i_stone() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/stone/i/one", 10)
                .withResourceLocation("goblin_caves/stone/i/two", 10)
                .withResourceLocation("goblin_caves/stone/i/three", 10)
                .withResourceLocation("goblin_caves/stone/i/pillar", 1)
                .withResourceLocation("goblin_caves/stone/i/pillar_broken", 2)
                .withResourceLocation("goblin_caves/stone/i/granite", 1)
                .withResourceLocation("goblin_caves/stone/i/diorite", 1)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }
    private static AbstractGridRoom l_stone() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/stone/l/one", 10)
                .withResourceLocation("goblin_caves/stone/l/two", 10)
                .withResourceLocation("goblin_caves/stone/l/three", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }
    private static AbstractGridRoom l_stone_vein() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/stone/l/iron_vein", 8)
                .withResourceLocation("goblin_caves/stone/l/copper_vein", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static AbstractGridRoom l_blackstone_vein() {
        return new GridRoomMultiResource(11, 11)
                .withResourceLocation("goblin_caves/blackstone/l/gold_vein", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    @Override
    public GridRoomCollection getCopy() {
        return new GoblinCavesGridRoomCollection();
    }
}
