package net.emsee.thedungeon.dungeon.registry;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup.CleanupLightGridRoomCollection;
import net.emsee.thedungeon.dungeon.src.types.grid.roomCollection.GridRoomCollection;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.dungeon.src.types.grid.GridDungeon;
import net.emsee.thedungeon.dungeon.registry.roomCollections.cleanup.CleanupGridRoomCollection;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCleanupDungeons {
    // on true, wipes the complete dungeon. on false only removes illegal blocks
    private static final boolean fullCleanup = false;

    public static final DeferredRegister<Dungeon<?,?>> DUNGEONS =
            DeferredRegister.create(ModDungeons.DUNGEON_REGISTRY, TheDungeon.MOD_ID);

    private static final GridRoomCollection collection = fullCleanup?new CleanupGridRoomCollection(): new CleanupLightGridRoomCollection();

    static final int cleanupDepth = 8;
    static final int cleanupHeight = 8;

    static final int cleanupCellWidth = fullCleanup?47:51;
    static final int cleanupCellHeight = fullCleanup?47:51;

    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_F = register("cleanup_f", ()->new GridDungeon(
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

    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_E = register("cleanup_e", ()->new GridDungeon(
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

    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_D = register("cleanup_d", ()->new GridDungeon(
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
    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_C = register("cleanup_c", ()->new GridDungeon(
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
    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_B = register("cleanup_b", ()->new GridDungeon(
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
    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_A = register("cleanup_a", ()->new GridDungeon(
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
    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_S = register("cleanup_s", ()->new GridDungeon(
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
    public static final DeferredHolder<Dungeon<?,?>, GridDungeon> CLEANUP_SS = register("cleanup_ss", ()->new GridDungeon(
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


    protected static <T extends Dungeon<?,?>> DeferredHolder<Dungeon<?,?>, T> register(String name, Supplier<T> dungeon) {
        DebugLog.logInfo(DebugLog.DebugType.INSTANCE_SETUP, "Registering Dungeon :{}", dungeon);
        return DUNGEONS.register(name, dungeon);
    }

    public static void register(IEventBus eventBus) {
        DUNGEONS.register(eventBus);
    }


}
