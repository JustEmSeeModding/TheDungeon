package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.dungeon.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.mobSpawnRules.rules.SpawnInBox;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.emsee.thedungeon.structureProcessor.goblinCaves.GildedCaveOreProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.StoneToGildedCaveProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.StoneCaveOreProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public final class GoblinCavesGridRoomCollection extends GridRoomCollection {
    public GoblinCavesGridRoomCollection() {
        super(11, 11);
        //SetFallback(new GridRoom(11, 11).ResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "castle/fallback")))
        this
                .addRequiredRoomsOf(4, 6, spawnRooms())
                .addRequiredRoomsOf(5, dens())


                .addRequiredRoom(0, 1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_gilded")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(GridRoomUtils.Connection.north, "stone_caves")
                        .setConnectionTag(GridRoomUtils.Connection.south, "gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))

                .addRooms(stone_caves_biome())
                .addRooms(gilded_caves_biome())

                .addTagRule(new WallFailRule("stone_caves", 11, 11, 0, false, () -> Blocks.STONE, 11 * 11)
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addTagRule(new WallFailRule("gilded_caves", 11, 11, 0, false, () -> Blocks.BLACKSTONE, 11 * 11)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE))
        ;
    }

    private static List<GridRoom> spawnRooms() {
        return List.of(
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/spawn")
                        .withWeight(1)
                        .addConnection(GridRoomUtils.Connection.north)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/spawn")
                        .withWeight(1)
                        .addConnection(GridRoomUtils.Connection.north)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
        );
    }

    private static List<GridRoom> dens() {
        return List.of(
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/den")
                        .withWeight(1)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 2, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .addSpawnRule(new SpawnInBox<>(ModEntities.CAVE_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1)),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/den")
                        .withWeight(1)
                        .setSizeHeight(5, 5, 3)
                        .horizontalConnections(1, 1, 1, 0)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 2, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                        .addSpawnRule(new SpawnInBox<>(ModEntities.SHADOW_GOBLIN, new BlockPos(-22, 3, -22), new BlockPos(22, 27, 22), 2, 5, 1))
        );
    }

    private static List<GridRoom> stone_caves_biome() {
        return List.of(
                i_stone().withWeight(60),
                l_stone().withWeight(50),
                t_large_stone().withWeight(8),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/elevated_one")
                        .withWeight(4).setSizeHeight(3, 3, 2)
                        .horizontalConnections(1, 1, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/elevated_cliff")
                        .withWeight(4).setSizeHeight(3, 3, 2)
                        .horizontalConnections(1, 1, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/x_large/elevated_one")
                        .withWeight(3).setSizeHeight(3, 3, 2)
                        .horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/x_large/elevated_two")
                        .withWeight(3).setSizeHeight(3, 3, 2)
                        .horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/x_large/bridge")
                        .withWeight(2)
                        .setSizeHeight(3, 3, 2)
                        .horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)

        );
    }
    private static List<GridRoom> gilded_caves_biome() {
        return List.of(
                i_gilded().withWeight(45),
                l_gilded().withWeight(55),
                t_large_gilded().withWeight(8),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/elevated_one")
                        .withWeight(4).setSizeHeight(3, 3, 2)
                        .horizontalConnections(1, 1, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/elevated_cliff")
                        .withWeight(4).setSizeHeight(3, 3, 2)
                        .horizontalConnections(1, 1, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/x_large/elevated_one")
                        .withWeight(3).setSizeHeight(3, 3, 2)
                        .horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/x_large/elevated_two")
                        .withWeight(3).setSizeHeight(3, 3, 2)
                        .horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE),
                new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/x_large/bridge")
                        .withWeight(2)
                        .setSizeHeight(3, 3, 2)
                        .horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1)
                        .setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)

        );
    }

    private static GridRoomGroup t_large_stone() {
        return ((GridRoomGroup) new GridRoomGroup(11, 11).setSizeHeight(3, 3, 1).horizontalConnections(1, 1, 0, 1).setAllConnectionTags("stone_caves").doAllowRotation())
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/one")
                        .withWeight(2)
                        .setSizeHeight(3, 3, 1)
                        .horizontalConnections(1, 1, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                )
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/crevice")
                        .withWeight(2)
                        .setSizeHeight(3, 3, 1)
                        .horizontalConnections(1, 1, 0, 1)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                );
    }
    private static GridRoomGroup t_large_gilded() {
        return ((GridRoomGroup) new GridRoomGroup(11, 11).setSizeHeight(3, 3, 1).horizontalConnections(1, 1, 0, 1).setAllConnectionTags("gilded_caves").doAllowRotation())
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/one")
                        .withWeight(2)
                        .setSizeHeight(3, 3, 1)
                        .horizontalConnections(1, 1, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                )
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/t_large/crevice")
                        .withWeight(2)
                        .setSizeHeight(3, 3, 1)
                        .horizontalConnections(1, 1, 0, 1)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE)
                );
    }
    private static GridRoomGroup i_stone() {
        return ((GridRoomGroup) new GridRoomGroup(11, 11).horizontalConnections(1, 0, 1, 0).setAllConnectionTags("stone_caves").doAllowRotation())
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/one")
                        .withWeight(10)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/two")
                        .withWeight(10)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/three")
                        .withWeight(10)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/pillar")
                        .withWeight(1)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/pillar_broken")
                        .withWeight(2)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE));
    }
    private static GridRoomGroup i_gilded() {
        return ((GridRoomGroup) new GridRoomGroup(11, 11).horizontalConnections(1, 0, 1, 0).setAllConnectionTags("gilded_caves").doAllowRotation())
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/one")
                        .withWeight(10)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/two")
                        .withWeight(10)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/three")
                        .withWeight(10)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/pillar")
                        .withWeight(1)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/i/pillar_broken")
                        .withWeight(2)
                        .horizontalConnections(1, 0, 1, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE));
    }
    private static GridRoomGroup l_stone() {
        return ((GridRoomGroup) new GridRoomGroup(11, 11).horizontalConnections(1, 1, 0, 0).setAllConnectionTags("stone_caves").doAllowRotation())
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/l/one")
                        .withWeight(10)
                        .horizontalConnections(1, 1, 0, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/l/two")
                        .withWeight(10)
                        .horizontalConnections(1, 1, 0, 0)
                        .setAllConnectionTags("stone_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE));
    }
    private static GridRoomGroup l_gilded() {
        return ((GridRoomGroup) new GridRoomGroup(11, 11).horizontalConnections(1, 1, 0, 0).setAllConnectionTags("gilded_caves").doAllowRotation())
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/l/one")
                        .withWeight(10)
                        .horizontalConnections(1, 1, 0, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE))
                .addRoom(new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/stone/l/two")
                        .withWeight(10)
                        .horizontalConnections(1, 1, 0, 0)
                        .setAllConnectionTags("gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneToGildedCaveProcessor.INSTANCE));
    }


    @Override
    public GridRoomCollection getCopy() {
        return new GoblinCavesGridRoomCollection();
    }
}
