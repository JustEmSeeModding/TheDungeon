package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DungeonItem extends Item implements IDungeonCarryItem {

    public DungeonItem(Item.Properties pProperties) {
        super(pProperties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
