package net.emsee.thedungeon.utils;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.datagen.ModBlockTagProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> DUNGEON_TOOL_MINABLE = createTag("dungeon_tool_minable");

        //TIER 0 = wood/gold, TIER 1 = stone, TIER 2 = iron, TIER 3 = diamond TIER 4 = netherite
        //public static final TagKey<Block> NEEDS_NETHERITE_TOOL = createTag("needs_netherite_tool");
        public static final MinableTag DUNGEON_TIER_0 = new MinableTag("dungeon_mining_tier_0", 0);
        public static final MinableTag DUNGEON_TIER_1 = new MinableTag("dungeon_mining_tier_1", 1);
        public static final MinableTag DUNGEON_TIER_2 = new MinableTag("dungeon_mining_tier_2", 2);
        public static final MinableTag DUNGEON_TIER_3 = new MinableTag("dungeon_mining_tier_3", 3);
        public static final MinableTag DUNGEON_TIER_4 = new MinableTag("dungeon_mining_tier_4", 4);
        public static final MinableTag DUNGEON_TIER_5 = new MinableTag("dungeon_mining_tier_5", 5);
        public static final MinableTag DUNGEON_TIER_6 = new MinableTag("dungeon_mining_tier_6", 6);

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(TheDungeon.defaultResourceLocation(name));
        }

        public static class MinableTag {
            public final int LEVEL;
            public final TagKey<Block> needs;
            public final TagKey<Block> incorrect;

            public MinableTag(String name, int level) {
                LEVEL = level;
                if (level > 0)
                    this.needs = createTag("needs_"+name+"_tool");
                else
                    this.needs = null;
                this.incorrect = createTag("incorrect_for_"+name+"_tool");
                ModBlockTagProvider.miningLevels.add(this);
            }
        }
    }

    public static class Items {
        public static final TagKey<Item> CAN_CARRY_TO_DUNGEON = createTag("can_carry_to_dungeon");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(TheDungeon.defaultResourceLocation(name));
        }
    }


}
