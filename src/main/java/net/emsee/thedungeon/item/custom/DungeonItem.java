package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DungeonItem extends Item implements IDungeonCarryItem {
    public static final Component DUNGEON_ITEM_HOVER_MESSAGE = Component.translatable("item.thedungeon.dungeon_item").withStyle(ChatFormatting.DARK_PURPLE);

    public DungeonItem(Item.Properties pProperties) {
        super(pProperties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
