package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.dungeon.registry.ModDungeons;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.emsee.thedungeon.worldSaveData.DungeonSaveData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

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
        if (savedSeed!=null) {
            generateSeeded(level.getServer(),savedSeed);
        }
        long selectedSeed = GameruleRegistry.getIntegerGamerule(level.getServer(), ModGamerules.DUNGEON_SEED_OVERRIDE);
        if (selectedSeed == -1) {
            long newSeed = new Random().nextLong();
            generateSeeded(level.getServer(),newSeed);
            savedSeed=newSeed;
            DungeonSaveData.Get(level.getServer()).setDirty();
        } else {
            generateSeeded(level.getServer(),selectedSeed);
            savedSeed=selectedSeed;
            DungeonSaveData.Get(level.getServer()).setDirty();
        }
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
        String[] saveArray = save.split(":");

        Dungeon<?, ?> dungeon = ModDungeons.getByResourceName(saveArray[0]);
        DungeonInstance<?> toReturn = dungeon.createInstance();

        toReturn.loadSaveString(saveArray);

        return toReturn;

    }

    public abstract void loadSaveString(String[] saveArray);

    public abstract String toSaveString();
}
