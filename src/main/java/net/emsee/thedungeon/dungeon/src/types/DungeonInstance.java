package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.dungeon.registry.ModDungeons;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class DungeonInstance<T extends Dungeon<?,?>>{
    protected final T dungeon;
    protected Long savedSeed = null;

    protected DungeonInstance(T dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * starts dungeon generation.
     */
    public final void generate(ServerLevel level) {
        MinecraftServer server = level.getServer();
        // if a seed is still in memory, use that.
        if (savedSeed!=null) {
            generateSeeded(server,savedSeed);
            return;
        }
        // if override == -1 pick random seed, else use the override
        long override = GameruleRegistry.getIntegerGamerule(server, ModGamerules.DUNGEON_SEED_OVERRIDE);
        long seed = (override == -1) ? ThreadLocalRandom.current().nextLong() : override;
        generateSeeded(server, seed);
    }

    /**
     * generates the dungeon with a seed
     */
    public final void generateSeeded(MinecraftServer server, long seed) {
        savedSeed = seed;
        DungeonSaveData.Get(server).setDirty();
        localGenerateSeeded(seed);
    }

    protected abstract void localGenerateSeeded(long seed);

    /**
     * runs every tick while generating.
     */
    public abstract void generationTick(ServerLevel serverLevel);


    /**
     * returns true if the dungeon has successfully generated
     */
    public abstract boolean isDoneGenerating();

    /**
     * returns true if the dungeon has started but not yet finished generating
     */
    public abstract boolean isBusyGenerating();

    public abstract boolean canManualStepNow();

    public DungeonRank getRank() {
        return dungeon.getRank();
    }

    public T getRaw() {
        return dungeon;
    }

    public static DungeonInstance<?> fromSaveString(String save) {
        if (save == null || save.isEmpty()) {
            throw new IllegalArgumentException("Empty save string");
        }
        String[] saveArray = save.split(":");
        if (saveArray.length == 0) {
            throw new IllegalArgumentException("Malformed save: " + save);
        }
        Dungeon<?, ?> dungeon = ModDungeons.getByResourceName(saveArray[0]);
        if (dungeon == null) {
            throw new IllegalStateException("Unknown dungeon: " + saveArray[0]);
        }
        DungeonInstance<?> instance = dungeon.createInstance();
        instance.loadSaveString(saveArray);
        return instance;

    }

    public abstract void loadSaveString(String[] saveArray);

    public abstract String toSaveString();
}
