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
    /****
     * Constructs a Goblin Caves grid room collection with predefined spawn rooms, dens, biome-specific rooms, and wall rules.
     *
     * Initializes the collection with an 11x11 grid, adds required spawn and den rooms at specific coordinates, includes biome-themed room groups for stone and gilded caves, and sets up wall validation rules with appropriate structure processors.
     */
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

    /**
     * Returns a list of spawn room definitions for the goblin caves dungeon.
     *
     * Each room is 11x11 in size, configured with a north connection, rotation allowed, and a biome-specific structure processor.
     * One room is tagged for the "stone_caves" biome using the stone cave processor, and the other for the "gilded_caves" biome using the gilded cave processor.
     *
     * @return a list containing two biome-specific spawn rooms
     */
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

    /**
     * Returns a list of den rooms configured for the goblin caves dungeon, each with spawn rules for goblin entities.
     *
     * The returned list contains two 11x11 den rooms with specific size, connection offsets, rotation allowance, and structure processors.
     * One room is tagged for the "stone_caves" biome and spawns cave goblins; the other is tagged for the "gilded_caves" biome and spawns shadow goblins.
     *
     * @return a list of den rooms with associated goblin spawn rules for use in dungeon generation
     */
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

    /**
     * Returns a list of grid rooms and room groups themed for the stone caves biome.
     *
     * The returned list includes weighted room groups and individual rooms with various layouts, connection configurations, and structure processors for generating stone cave environments.
     *
     * @return a list of stone caves biome grid rooms and groups
     */
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
    /**
     * Returns a list of grid rooms and room groups themed for the gilded caves biome.
     *
     * The returned list includes weighted room groups and individual rooms, each configured with the "gilded_caves" tag, specific resource locations, connection settings, rotation allowance, and the StoneToGildedCaveProcessor for structure processing.
     *
     * @return a list of GridRoom instances representing the gilded caves biome layout options
     */
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

    /**
     * Returns a group of large T-shaped stone cave rooms for the dungeon grid.
     *
     * The group consists of two rooms with distinct layouts, both sized 11x11 with a height of 3, themed for the "stone_caves" biome. Each room supports horizontal connections, allows rotation, and uses the StoneCaveOreProcessor for structure processing.
     *
     * @return a GridRoomGroup containing large T-shaped stone cave rooms
     */
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
    /**
     * Creates a group of large T-shaped rooms themed for the gilded caves biome.
     *
     * The group contains two rooms with distinct layouts, both configured for gilded cave generation, supporting rotation and horizontal connections, and using the StoneToGildedCaveProcessor.
     *
     * @return a GridRoomGroup representing large T-shaped gilded cave rooms
     */
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
    /**
     * Creates a group of "I"-shaped stone cave rooms for the dungeon grid, each with horizontal connections and stone cave tags.
     *
     * The group includes several room variants with different resource locations and weights, all allowing rotation and using the StoneCaveOreProcessor.
     *
     * @return a GridRoomGroup containing weighted "I"-shaped stone cave rooms
     */
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
    /**
     * Creates a group of "I"-shaped gilded cave rooms for dungeon generation.
     *
     * The group contains multiple room variants with horizontal connections, all tagged for the "gilded_caves" biome and configured to allow rotation. Each room uses the StoneToGildedCaveProcessor to apply gilded cave features, and rooms are assigned different weights for random selection.
     *
     * @return a GridRoomGroup containing weighted "I"-shaped gilded cave rooms
     */
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
    /**
     * Creates a group of "L"-shaped stone cave rooms for the dungeon grid.
     *
     * The group contains two rooms with distinct layouts, both configured for horizontal connections, tagged as "stone_caves", allowing rotation, and using the StoneCaveOreProcessor for structure modification.
     *
     * @return a GridRoomGroup containing two weighted "L"-shaped stone cave rooms
     */
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
    /**
     * Creates a group of L-shaped grid rooms themed for the gilded caves biome.
     *
     * The group contains two rooms with distinct layouts, both supporting horizontal connections, rotation, and using the StoneToGildedCaveProcessor to apply gilded cave features.
     *
     * @return a GridRoomGroup containing L-shaped gilded cave rooms
     */
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


    /****
     * Creates and returns a new instance of {@code GoblinCavesGridRoomCollection} with the same configuration.
     *
     * @return a fresh copy of this grid room collection
     */
    @Override
    public GridRoomCollection getCopy() {
        return new GoblinCavesGridRoomCollection();
    }
}
