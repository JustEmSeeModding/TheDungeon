package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.dungeon.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.mobSpawnRules.rules.SpawnInBox;
import net.emsee.thedungeon.dungeon.room.MultiResourceGridRoom;
import net.emsee.thedungeon.dungeon.util.Connection;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.emsee.thedungeon.dungeon.room.GridRoomList;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.structureProcessor.goblinCaves.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public final class GoblinCavesGridRoomCollection extends GridRoomCollection {

    public GoblinCavesGridRoomCollection() {
        super(11, 11);
        this
                .setStartingRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/den")
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                        .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .addMobSpawnRule(new SpawnInBox<>(ModEntities.CAVE_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)))
                .addRequiredRoomsOf(35, 50, spawnRooms)
                .addRequiredRoomsOf(5, dens)

                .addRequiredRoom(0, 1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_blackstone")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, "stone_caves")
                        .setConnectionTag(Connection.SOUTH, "gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_blackstone")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, "deepslate_caves")
                        .setConnectionTag(Connection.SOUTH, "stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepslateCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToStoneCaveProcessor.INSTANCE)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_blackstone")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, "deepslate_caves")
                        .setConnectionTag(Connection.SOUTH, "gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepslateCaveProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(0, 1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_blackstone")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, "deepslate_caves")
                        .setConnectionTag(Connection.SOUTH, "ice_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepslateCaveProcessor.INSTANCE)
                        .withStructureProcessor(BlackstoneToStoneCaveProcessor.INSTANCE)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))
                .addRequiredRoom(1, 1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_overgrown")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(Connection.NORTH, "stone_caves")
                        .setConnectionTag(Connection.SOUTH, "overgrown_caves")
                        .doAllowRotation()
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))

                .addRooms(unassigned_caves.getCopy()
                        .applyToAll(room -> room.withStructureProcessor(new StoneCaveOreProcessor()))
                        .applyToAll(room -> room.setAllConnectionTags("stone_caves"))
                )
                .addRooms(unassigned_caves.getCopy()
                        .applyToAll(room -> room.withStructureProcessor(new StoneToGildedCaveProcessor()))
                        .applyToAll(room -> room.setAllConnectionTags("gilded_caves"))
                        .applyToAll(room -> room.setOverrideEndChance(.02f))
                )
                .addRooms(unassigned_caves.getCopy()
                        .applyToAll(room -> room.withStructureProcessor(new StoneToDeepslateCaveProcessor()))
                        .applyToAll(room -> room.setAllConnectionTags("deepslate_caves"))
                )
                .addRooms(unassigned_caves.getCopy()
                        .applyToAll(room -> room.withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags("ice_caves"))
                        .applyToAll(room -> room.setOverrideEndChance(.04f))
                )
                .addRooms(overgrown_caves.getCopy()
                        .applyToAll(room -> room.withStructureProcessor(OvergrownCaveProcessor.INSTANCE))
                        .applyToAll(room -> room.setAllConnectionTags("overgrown_caves"))
                )

                .addTagRule(new WallFailRule("stone_caves", 11, 11, 0, false, () -> Blocks.STONE, 11 * 11)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule("gilded_caves", 11, 11, 0, false, () -> Blocks.BLACKSTONE, 11 * 11)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule("deepslate_caves", 11, 11, 0, false, () -> Blocks.STONE, 11 * 11)
                        .withStructureProcessor(StoneToDeepslateCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule("ice_caves", 11, 11, 0, false, () -> Blocks.STONE, 11 * 11)
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                .addTagRule(new WallFailRule("overgrown_caves", 11, 11, 0, false, () -> Blocks.STONE, 11 * 11)
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE))
        ;
    }

    private static final GridRoomList spawnRooms =
        new GridRoomList().addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/spawn")
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/spawn")
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/spawn")
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags("deepslate_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToDeepslateCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/spawn")
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags("ice_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToIceCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/overgrown/spawn")
                        .withWeight(1)
                        .addConnection(Connection.NORTH)
                        .setAllConnectionTags("overgrown_caves")
                        .doAllowRotation()
                        .withStructureProcessor(OvergrownCaveProcessor.INSTANCE));



    private static final GridRoomList dens = new GridRoomList()
            .addRoom(new GridRoom(11, 11)
                    .withResourceLocation("goblin_caves/stone/den")
                    .setSizeHeight(5, 5, 3)
                    .horizontalConnections(1, 1, 1, 0)
                    .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                    .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                    .setAllConnectionTags("stone_caves")
                    .doAllowRotation()
                    .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                    .addMobSpawnRule(new SpawnInBox<>(ModEntities.CAVE_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)))
            .addRoom(new GridRoom(11, 11)
                    .withResourceLocation("goblin_caves/stone/den")
                    .setSizeHeight(5, 5, 3)
                    .horizontalConnections(1, 1, 1, 0)
                    .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                    .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                    .setAllConnectionTags("gilded_caves")
                    .doAllowRotation()
                    .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                    .addMobSpawnRule(new SpawnInBox<>(ModEntities.SHADOW_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)))
            .addRoom(new GridRoom(11, 11)
                    .withResourceLocation("goblin_caves/stone/den")
                    .setSizeHeight(5, 5, 3)
                    .horizontalConnections(1, 1, 1, 0)
                    .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                    .setHorizontalConnectionOffset(Connection.SOUTH, 2, 1)
                    .setAllConnectionTags("deepslate_caves")
                    .doAllowRotation()
                    .withStructureProcessor(StoneToDeepslateCaveProcessor.INSTANCE)
                    .addMobSpawnRule(new SpawnInBox<>(ModEntities.SHADOW_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)));

    private static final GridRoomList unassigned_caves = new GridRoomList()
            .addRoom(i_stone().withWeight(60))
            .addRoom(l_stone().withWeight(50))
            .addRoom(
                    new MultiResourceGridRoom(11, 11)
                            .withResourceLocation("goblin_caves/stone/t_large/one", 5)
                            .withResourceLocation("goblin_caves/stone/t_large/crevice", 2)
                            .withWeight(8)
                            .setSizeHeight(3, 3, 1)
                            .horizontalConnections(1, 1, 0, 1)
                            .doAllowRotation())
            .addRoom(
                    new MultiResourceGridRoom(11, 11)
                            .withResourceLocation("goblin_caves/stone/t_large/elevated_one", 2)
                            .withResourceLocation("goblin_caves/stone/t_large/elevated_cliff", 1)
                            .withWeight(8)
                            .setSizeHeight(3, 3, 2)
                            .horizontalConnections(1, 1, 0, 1)
                            .setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                            .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                            .doAllowRotation())
            .addRoom(
                    new GridRoom(11, 11)
                    .withResourceLocation("goblin_caves/stone/x_large/elevated_one")
                    .withWeight(3)
                    .setSizeHeight(3, 3, 2)
                    .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                    .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                    .doAllowRotation())
            .addRoom(// TODO has an issue with some misplaced blocks
                    new GridRoom(11, 11)
                    .withResourceLocation("goblin_caves/stone/x_large/elevated_two")
                    .withWeight(3).setSizeHeight(3, 3, 2)
                    .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                    .setHorizontalConnectionOffset(Connection.SOUTH, 0, 1)
                    .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                    .doAllowRotation())
            .addRoom(
                    new GridRoom(11, 11)
                    .withResourceLocation("goblin_caves/stone/x_large/bridge")
                    .withWeight(2)
                    .setSizeHeight(3, 3, 2)
                    .horizontalConnections().setHorizontalConnectionOffset(Connection.EAST, 0, 1)
                    .setHorizontalConnectionOffset(Connection.WEST, 0, 1)
                    .doAllowRotation()
            );

    private static GridRoom i_stone() {
        return new MultiResourceGridRoom(11, 11)
                .withResourceLocation("goblin_caves/stone/i/one", 10)
                .withResourceLocation("goblin_caves/stone/i/two", 10)
                .withResourceLocation("goblin_caves/stone/i/three", 10)
                .withResourceLocation("goblin_caves/stone/i/pillar", 1)
                .withResourceLocation("goblin_caves/stone/i/pillar_broken", 2)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static GridRoom l_stone() {
        return new MultiResourceGridRoom(11, 11)
                .withResourceLocation("goblin_caves/stone/l/one", 10)
                .withResourceLocation("goblin_caves/stone/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static final GridRoomList overgrown_caves = new GridRoomList()
            .addRoom(i_overgrown().withWeight(60))
            .addRoom(l_overgrown().withWeight(50))
            .addRoom(t_overgrown().withWeight(30));

    private static GridRoom i_overgrown() {
        return new MultiResourceGridRoom(11, 11)
                .withResourceLocation("goblin_caves/overgrown/i/one", 10)
                .withResourceLocation("goblin_caves/overgrown/i/two", 10)
                .horizontalConnections(1, 0, 1, 0)
                .doAllowRotation();
    }

    private static GridRoom l_overgrown() {
        return new MultiResourceGridRoom(11, 11)
                .withResourceLocation("goblin_caves/overgrown/l/one", 10)
                .withResourceLocation("goblin_caves/overgrown/l/two", 10)
                .horizontalConnections(1, 1, 0, 0)
                .doAllowRotation();
    }

    private static GridRoom t_overgrown() {
        return new MultiResourceGridRoom(11, 11)
                .withResourceLocation("goblin_caves/overgrown/t/one", 10)
                .withResourceLocation("goblin_caves/overgrown/t/two", 10)
                .horizontalConnections(1, 0, 1, 1)
                .doAllowRotation();
    }

    @Override
    public GridRoomCollection getCopy() {
        return new GoblinCavesGridRoomCollection();
    }
}
