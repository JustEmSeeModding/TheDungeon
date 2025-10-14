package net.emsee.thedungeon.dungeon.registry;

import net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup.CleanupLightGridRoomCollection;
import net.emsee.thedungeon.dungeon.src.types.roomCollection.GridRoomCollection;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.dungeon.src.types.grid.GridDungeon;
import net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup.CleanupGridRoomCollection;
import net.emsee.thedungeon.dungeon.src.DungeonRank;

public class ModCleanupDungeons extends ModDungeons {
    // on true, wipes the complete dungeon. on false only removes illegal blocks
    private static final boolean fullCleanup = false;
    
    private static final GridRoomCollection collection = fullCleanup?new CleanupGridRoomCollection(): new CleanupLightGridRoomCollection();

    static final int cleanupDepth = 8;
    static final int cleanupHeight = 8;

    static final int cleanupCellWidth = fullCleanup?47:51;
    static final int cleanupCellHeight = fullCleanup?47:51;

    public static final Dungeon CLEANUP_F = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_f",
            DungeonRank.F,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));

    public static final Dungeon CLEANUP_E = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_e",
            DungeonRank.E,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));

    public static final Dungeon CLEANUP_D = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_d",
            DungeonRank.D,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));
    public static final Dungeon CLEANUP_C = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_c",
            DungeonRank.C,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));
    public static final Dungeon CLEANUP_B = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_b",
            DungeonRank.B,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));
    public static final Dungeon CLEANUP_A = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_a",
            DungeonRank.A,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));
    public static final Dungeon CLEANUP_S = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_s",
            DungeonRank.S,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));
    public static final Dungeon CLEANUP_SS = register(new GridDungeon(
            "dungeon.the_dungeon.cleanup_ss",
            DungeonRank.SS,
            0,
            cleanupCellWidth,
            cleanupCellHeight,
            collection)
            .allowDownGeneration(false)
            .setDepth(cleanupDepth)
            .setMaxFloorHeightOneWay(cleanupHeight)
            .setRoomEndChance(0)
            .setOverrideGenerationHeight(-96)
            .setUtilDungeon(true));
}
