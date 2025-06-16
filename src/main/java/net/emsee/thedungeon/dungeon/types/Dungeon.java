package net.emsee.thedungeon.dungeon.types;

import net.emsee.thedungeon.dungeon.util.DungeonRank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public abstract class Dungeon {
    private static int lastID = 0;
    protected final String resourceName;
    private final int ID;
    protected boolean utilDungeon = false;
    protected final DungeonRank rank;
    protected final int weight;
    protected BlockPos overrideCenter = null;

    // constructor
    public Dungeon(String resourceName, DungeonRank rank, int weight) {
        //DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Creating dungeon class :{}", getResourceName());
        ID = lastID++;
        this.resourceName = resourceName;
        this.rank = rank;
        this.weight = weight;
    }

    protected Dungeon(String resourceName, DungeonRank rank, int weight, int ID) {
        //DebugLog.logInfo(DebugLog.DebugLevel.INSTANCE_SETUP,"Creating dungeon class :{}", getResourceName());
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
        if (overrideCenter==null){overrideCenter = rank.getDefaultCenterPos();}
        overrideCenter = overrideCenter.atY(height);
        return this;
    }

    protected Dungeon setOverrideCenter(BlockPos override) {
        overrideCenter=override;
        return this;
    }

    // methods

    /**
     * starts dungeon generation.
     */
    public abstract void generate(ServerLevel serverLevel, BlockPos worldPos);

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
    };

    protected abstract Dungeon getCopy(int ID);

    public abstract boolean isDoneGenerating();

    /**
     * returns true if the dungeon has started but not yet finished generating
     */
    public abstract boolean isBusyGenerating();

    @Override
    public String toString() {
        return (getResourceName() + "-weight:" + getWeight());
    }
}
