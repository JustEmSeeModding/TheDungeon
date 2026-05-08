package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.dungeon.src.DungeonRank;

public class DungeonCatalystBlock extends DungeonBlock {
    final DungeonRank catalistRank;

    public DungeonCatalystBlock(DungeonRank catalistRank, Properties properties) {
        super(properties);
        this.catalistRank = catalistRank;
    }

    public DungeonRank getCatalistRank() {
        return catalistRank;
    }
}
