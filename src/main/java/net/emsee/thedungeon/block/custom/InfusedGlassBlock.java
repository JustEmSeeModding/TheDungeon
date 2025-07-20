package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.StainedGlassBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfusedGlassBlock extends StainedGlassBlock implements IDungeonCarryItem {
    public InfusedGlassBlock(Properties properties) {
        super(DyeColor.CYAN, properties);
    }

    @Override
    public void appendHoverText( ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents,  TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
