package net.emsee.thedungeon.block.custom;

import net.emsee.thedungeon.item.custom.DungeonItem;
import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.BaseEntityBlock;

import java.util.List;

public abstract class BaseDungeonEntityBlock extends BaseEntityBlock implements ICanTakeItemToDungeon {
    protected BaseDungeonEntityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item. TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
