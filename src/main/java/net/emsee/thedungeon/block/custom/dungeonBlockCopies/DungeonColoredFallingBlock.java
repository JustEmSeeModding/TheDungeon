package net.emsee.thedungeon.block.custom.dungeonBlockCopies;

import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.ColoredFallingBlock;

import java.util.List;

public class DungeonColoredFallingBlock extends ColoredFallingBlock implements IDungeonCarryItem {
    public DungeonColoredFallingBlock(ColorRGBA dustColor, Properties properties) {
        super(dustColor, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
