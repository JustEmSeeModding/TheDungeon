package net.emsee.thedungeon.item.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;

/**
 * attach this to any block or item that should be allowed into the dungeon
 */
public interface ICanTakeItemToDungeon {
    Component DUNGEON_ITEM_HOVER_MESSAGE = Component.translatable("item.thedungeon.tooltip.dungeon_item").withStyle(ChatFormatting.DARK_PURPLE);
    Rarity DUNGEON_ITEM_RARITY = Rarity.RARE;
}
