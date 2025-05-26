package net.emsee.thedungeon.item.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

/**
 * attach this to any block or item that should be allowed into the dungeon
 */
public interface IDungeonCarryItem {
    Component DUNGEON_ITEM_HOVER_MESSAGE = Component.translatable("item.thedungeon.tooltip.dungeon_item").withStyle(ChatFormatting.DARK_PURPLE);
}
