package net.emsee.thedungeon.dungeon.roomCollections.collections;

import net.emsee.thedungeon.dungeon.connectionRules.fail.WallFailRule;
import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.dungeon.room.GridRoomGroup;
import net.emsee.thedungeon.dungeon.room.GridRoomUtils;
import net.emsee.thedungeon.dungeon.roomCollections.GridRoomCollection;
import net.emsee.thedungeon.structureProcessor.goblinCaves.GildedCaveOreProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.StoneToGildedCaveProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.StoneCaveOreProcessor;
import net.minecraft.world.level.block.Blocks;

public final class GoblinCavesGridRoomCollection extends GridRoomCollection {
    public GoblinCavesGridRoomCollection() {
        super(11, 11);
        //SetFallback(new GridRoom(11, 11).ResourceLocation(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "castle/fallback")))
        /*this.SetStartingRoom(new GridRoom(9, 9)
                .SetSizeHeight(3, 3, 4)
                .ResourceLocation("goblin_caves/mainhall/center")
                .HorizontalConnections().SetAllConnectionTags("main_hall")
                .SetOverrideEndChance(0).SetGenerationPriority(10)
                .addSpawnRule(new SpawnAt(EntityType.ZOGLIN, new BlockPos(0, 3, 0))))*/
        this.addRoom(new GridRoom(11, 11).withResourceLocation("goblin_caves/stone/x_large/elevated_one").withWeight(2).setSizeHeight(3, 3, 2).horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1).setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 0, 1).setAllConnectionTags("stone_caves").doAllowRotation()
                .withStructureProcessor(StoneCaveOreProcessor.INSTANCE))

                .addRequiredRoom(0,1, new GridRoom(11, 11)
                        .withResourceLocation("goblin_caves/convert/stone_gilded")
                        .withWeight(3)
                        .horizontalConnections(1, 0, 1, 0)
                        .setConnectionTag(GridRoomUtils.Connection.north,"stone_caves")
                        .setConnectionTag(GridRoomUtils.Connection.south,"gilded_caves")
                        .doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                        .withStructureProcessor(GildedCaveOreProcessor.INSTANCE)
                        .setOverrideEndChance(0)
                        .setGenerationPriority(1))


                .addRoom(new GridRoom(11, 11).withResourceLocation("goblin_caves/stone/x_large/elevated_two").withWeight(2).setSizeHeight(3, 3, 2).horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1).setHorizontalConnectionOffset(GridRoomUtils.Connection.south, 0, 1).setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1).setAllConnectionTags("stone_caves").doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                )
                .addRoom(new GridRoom(11, 11).withResourceLocation("goblin_caves/stone/x_large/bridge").withWeight(2).setSizeHeight(3, 3, 2).horizontalConnections().setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1).setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1).setAllConnectionTags("stone_caves").doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                )
                .addRoom(new GridRoom(11, 11).withResourceLocation("goblin_caves/stone/t_large/elevated_one").withWeight(2).setSizeHeight(3, 3, 2).horizontalConnections(1,1,0,1).setHorizontalConnectionOffset(GridRoomUtils.Connection.east, 0, 1).setHorizontalConnectionOffset(GridRoomUtils.Connection.west, 0, 1).setAllConnectionTags("stone_caves").doAllowRotation()
                        .withStructureProcessor(StoneCaveOreProcessor.INSTANCE)
                )

                .addRoom(t_large_stone().withWeight(2))
                .addRoom(t_large_gilded().withWeight(2))
                .addRoom(i_stone().withWeight(5))
                .addRoom(i_gilded().withWeight(5))
                .addRoom(l_stone().withWeight(5))
                .addRoom(l_gilded().withWeight(5))

                .addTagRule(new WallFailRule("stone_caves", 11, 11, 0, false, () -> Blocks.STONE, 11 * 11))
                .addTagRule(new WallFailRule("gilded_caves", 11, 11, 0, false, () -> Blocks.BLACKSTONE, 11 * 11))
        ;
    }

    private static GridRoom t_large_stone() {
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

    private static GridRoom t_large_gilded() {
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

    private static GridRoom i_stone() {
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

    private static GridRoom i_gilded() {
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

    private static GridRoom l_stone() {
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

    private static GridRoom l_gilded() {
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
