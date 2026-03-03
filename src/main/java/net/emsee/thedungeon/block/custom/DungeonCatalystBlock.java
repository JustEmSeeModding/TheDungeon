package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

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
