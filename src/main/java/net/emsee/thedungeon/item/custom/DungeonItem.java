package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.ICanTakeItemToDungeon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class DungeonItem extends Item implements ICanTakeItemToDungeon {

    public DungeonItem(Item.Properties pProperties) {
        super(pProperties.rarity(ICanTakeItemToDungeon.DUNGEON_ITEM_RARITY));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}
