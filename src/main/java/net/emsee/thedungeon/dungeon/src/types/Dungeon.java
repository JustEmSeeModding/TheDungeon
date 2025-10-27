package net.emsee.thedungeon.dungeon.src.types;

import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public abstract class Dungeon<T extends Dungeon<?,?>, I extends DungeonInstance<T>> {
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

    public Dungeon setUtilDungeon(boolean is) {
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

    public boolean isUtilDungeon() {
        return utilDungeon;
    }

    @Override
    public String toString() {
        return (getResourceName() + "-weight:" + getWeight());
    }

    public static int getMaxID() {
        return lastID;
    }

    public abstract I createInstance();
}
