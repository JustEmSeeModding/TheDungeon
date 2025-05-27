package net.emsee.thedungeon.dungeon;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.dungeon.Dungeon;
import net.emsee.thedungeon.dungeon.dungeon.type.GridDungeon;
import net.emsee.thedungeon.dungeon.roomCollections.collections.*;

import java.util.HashMap;
import java.util.Map;


//TODO Rewrite dungeons and generate to generate based on rank. so there is always a dungeon of each rank open
public final class ModDungeons {
    public static final Map<String, Dungeon> DUNGEONS = new HashMap<>();


    public static final Dungeon TEST = register(new GridDungeon(
            "dungeon.the_dungeon.test",
            Dungeon.DungeonRank.F,
            0,
            3,
            3,
            new TestGridRoomCollection())
            .setDepth(10)
            .setRoomEndChance(0));


    public static final Dungeon THE_LIBRARY = register(new GridDungeon(
            "dungeon.the_dungeon.library",
            Dungeon.DungeonRank.C,
            1,
            13,
            9,
            new LibraryGridRoomCollection())
            .setDepth(20)
            .setRoomEndChance(.03f)
            .setMaxFloorHeight(3)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final Dungeon CASTLE = register(new GridDungeon(
            "dungeon.the_dungeon.castle",
            Dungeon.DungeonRank.B,
            1,
            9,
            9,
            new CastleGridRoomCollection())
            .setDepth(25)
            .setRoomEndChance(.02f)
            .setMaxFloorHeight(7)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final Dungeon GOBLIN_CAVES = register(new GridDungeon(
            "dungeon.the_dungeon.goblin_caves",
            Dungeon.DungeonRank.F,
            0,
            11,
            11,
            new GoblinCavesGridRoomCollection())
            .setDepth(25)
            .setRoomEndChance(.01f)
            .setMaxFloorHeight(12)
            .setRoomPickMethod(GridDungeon.RoomGenerationPickMethod.RANDOM));

    public static final Dungeon CLEANUP_F = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_f",
            Dungeon.DungeonRank.F,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));

    public static final Dungeon CLEANUP_E = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_e",
            Dungeon.DungeonRank.E,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));

    public static final Dungeon CLEANUP_D = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_d",
            Dungeon.DungeonRank.D,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));
    public static final Dungeon CLEANUP_C = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_c",
            Dungeon.DungeonRank.C,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));
    public static final Dungeon CLEANUP_B = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_b",
            Dungeon.DungeonRank.B,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));
    public static final Dungeon CLEANUP_A = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_a",
            Dungeon.DungeonRank.A,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));
    public static final Dungeon CLEANUP_S = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_s",
            Dungeon.DungeonRank.S,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));
    public static final Dungeon CLEANUP_SS = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_ss",
            Dungeon.DungeonRank.SS,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .IsUtilDungeon(true));

    private static Dungeon register(Dungeon dungeon) {
        if(TheDungeon.debugMode.is(TheDungeon.DebugMode.IMPORTANT_ONLY)) TheDungeon.LOGGER.info("Registering Dungeon :{}", dungeon);
        DUNGEONS.put(dungeon.GetResourceName(), dungeon);
        GlobalDungeonManager.dungeons.put(dungeon, dungeon.getWeight());
        return dungeon;
    }

    public static Dungeon GetByResourceName(String resourceName) {
        return DUNGEONS.get(resourceName);
    }
}
