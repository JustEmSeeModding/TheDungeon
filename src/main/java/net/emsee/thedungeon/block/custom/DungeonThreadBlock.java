package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TripWireBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DungeonThreadBlock extends TripWireBlock implements IDungeonCarryItem {
    public DungeonThreadBlock(Block hook, Properties properties) {
        super(hook, properties);
    }

    @Override
    public void appendHoverText( ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents,  TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
