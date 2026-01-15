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

public class DungeonCatalistBlock extends Block implements IDungeonCarryItem {
    final DungeonRank catalistRank;

    public DungeonCatalistBlock(DungeonRank catalistRank, Properties properties) {
        super(properties);
        this.catalistRank = catalistRank;
    }

    @Override
    public void appendHoverText( ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents,  TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public DungeonRank getCatalistRank() {
        return catalistRank;
    }
}
