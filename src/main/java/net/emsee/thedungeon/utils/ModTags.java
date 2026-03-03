package net.emsee.thedungeon.utils;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> DUNGEON_TOOL_MINABLE = createTag("dungeon_tool_minable");
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(TheDungeon.defaultResourceLocation(name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_CARRY_TO_DUNGEON = createTag("can_carry_to_dungeon");
        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(TheDungeon.defaultResourceLocation(name));
        }
    }
}
