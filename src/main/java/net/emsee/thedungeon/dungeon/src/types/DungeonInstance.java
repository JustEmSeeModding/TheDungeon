package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.dungeon.src.generators.DungeonGenerator;
import net.emsee.thedungeon.dungeon.src.generators.GridDungeonGenerator;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

public abstract class DungeonInstance<T extends Dungeon<?,?>>{
    protected final T dungeon;

    protected DungeonInstance(T dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * starts dungeon generation.
     */
    public final void generate(ServerLevel level) {
        long selectedSeed = GameruleRegistry.getIntegerGamerule(level.getServer(), ModGamerules.DUNGEON_SEED_OVERRIDE);
        if (selectedSeed == -1) {
            generateSeeded(new Random().nextLong());
        } else {
            generateSeeded(selectedSeed);
        }
    }

    /**
     * generates the dungeon with a seed
     */
    public abstract void generateSeeded(long seed);

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
}
