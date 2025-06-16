package net.emsee.thedungeon.dungeon;

import net.emsee.thedungeon.dungeon.types.Dungeon;
import net.emsee.thedungeon.dungeon.types.GridDungeon;
import net.emsee.thedungeon.dungeon.roomCollections.collections.CleanupGridRoomCollection;
import net.emsee.thedungeon.dungeon.util.DungeonRank;

public class ModCleanupDungeons extends ModDungeons {
    public static final Dungeon CLEANUP_F = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_f",
            DungeonRank.F,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));

    public static final Dungeon CLEANUP_E = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_e",
            DungeonRank.E,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));

    public static final Dungeon CLEANUP_D = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_d",
            DungeonRank.D,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));
    public static final Dungeon CLEANUP_C = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_c",
            DungeonRank.C,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));
    public static final Dungeon CLEANUP_B = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_b",
            DungeonRank.B,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));
    public static final Dungeon CLEANUP_A = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_a",
            DungeonRank.A,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));
    public static final Dungeon CLEANUP_S = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_s",
            DungeonRank.S,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));
    public static final Dungeon CLEANUP_SS = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_ss",
            DungeonRank.SS,
            0,
            47,
            47,
            new CleanupGridRoomCollection())
            .setDepth(7)
            .setMaxFloorHeight(15)
            .setRoomEndChance(0)
            .isUtilDungeon(true));
}
