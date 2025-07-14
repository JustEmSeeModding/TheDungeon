package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.gameRule.GameruleRegistry;
import net.emsee.thedungeon.gameRule.ModGamerules;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

public abstract class Dungeon {
    private static int lastID = -1;
    protected final String resourceName;
    private final int ID;
    protected boolean utilDungeon = false;
    protected final DungeonRank rank;
    protected final int weight;
    protected BlockPos overrideCenter = null;

    // constructor
    public Dungeon(String resourceName, DungeonRank rank, int weight) {
        if (resourceName.isEmpty()) throw new IllegalStateException(this + ":Dungeon cant have empty resource name");
        if (weight < 0) throw new IllegalStateException(this + ":Dungeon weight cant be negative");
        ID = ++lastID;
        this.resourceName = resourceName;
        this.rank = rank;
        this.weight = weight;
    }

    protected Dungeon(String resourceName, DungeonRank rank, int weight, int ID) {
        if (resourceName.isEmpty()) throw new IllegalStateException(this + ":Dungeon cant have empty resource name");
        if (weight < 0) throw new IllegalStateException(this + ":Dungeon weight cant be negative");
        this.ID = ID;
        this.resourceName = resourceName;
        this.rank = rank;
        this.weight = weight;
    }

    // constructionMethod

    public Dungeon isUtilDungeon(boolean is) {
        utilDungeon = is;
        return this;
    }

    public Dungeon setOverrideGenerationHeight(int height) {
        if (overrideCenter == null) {
            overrideCenter = rank.getDefaultCenterPos();
        }
        overrideCenter = overrideCenter.atY(height);
        return this;
    }

    protected Dungeon setOverrideCenter(BlockPos override) {
        overrideCenter = override;
        return this;
    }

    // methods

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

    public final String getResourceName() {
        return resourceName;
    }

    public final Component GetTranslatedName() {
        return Component.translatable(resourceName);
    }

    public DungeonRank getRank() {
        return rank;
    }

    public Integer getWeight() {
        return weight;
    }

    public int getID() {
        return ID;
    }

    public BlockPos getCenterPos() {
        if (overrideCenter != null)
            return overrideCenter;
        return rank.getDefaultCenterPos();
    }

    /**
     * gets a copy of base dungeon
     */
    public final Dungeon getCopy() {
        return getCopy(ID);
    }

    protected abstract Dungeon getCopy(int ID);

    /**
     * returns true if the dungeon has successfully generated
     */
    public abstract boolean isDoneGenerating();

    /**
     * returns true if the dungeon has started but not yet finished generating
     */
    public abstract boolean isBusyGenerating();

    @Override
    public String toString() {
        return (getResourceName() + "-weight:" + getWeight());
    }

    public static int getMaxID() {
        return lastID;
    }
}
