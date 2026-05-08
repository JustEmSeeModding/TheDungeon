package net.emsee.thedungeon.item;

import net.emsee.thedungeon.utils.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public enum ModTiers implements Tier {
    /*
    vanilla tiers for reference
    WOOD(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 59, 2.0F, 0.0F, 15, () -> Ingredient.of(ItemTags.PLANKS)),
    STONE(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS)),
    IRON(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(new ItemLike[]{Items.IRON_INGOT})),
    DIAMOND(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(new ItemLike[]{Items.DIAMOND})),
    GOLD(BlockTags.INCORRECT_FOR_GOLD_TOOL, 32, 12.0F, 0.0F, 22, () -> Ingredient.of(new ItemLike[]{Items.GOLD_INGOT})),
    NETHERITE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 4.0F, 15, () -> Ingredient.of(new ItemLike[]{Items.NETHERITE_INGOT}));

     */

    // I use 0 damage to make the damage numbers on the tools easier to understand.
    // Enchantment value is also 0 since these tools won't be enchantable by normal means.
    INFUSED_ALLOY(ModTags.Blocks.DUNGEON_TIER_2, 1800, 4f, 0f, 0, () -> Ingredient.of(ModItems.INFUSED_ALLOY_INGOT.get())),
    GILDREAN(ModTags.Blocks.DUNGEON_TIER_0, 80, 15, 0, 0, () -> Ingredient.of(ModItems.GILDREAN.INGOT.get())),
    ARCTIC_IRON(ModTags.Blocks.DUNGEON_TIER_3, 1700, 7.5f, 0, 0 , () -> Ingredient.of(ModItems.ARCTIC_IRON.INGOT.get())),
    LAVINTINE(ModTags.Blocks.DUNGEON_TIER_4, 2300, 9f, 0, 0 , () -> Ingredient.of(ModItems.LAVINTINE.INGOT.get())),
    INFERNAL_TIN(ModTags.Blocks.DUNGEON_TIER_5, 3000, 10.0f, 0, 0, () -> Ingredient.of(ModItems.INFERNAL_TIN.INGOT.get())),
    KOBALT(ModTags.Blocks.DUNGEON_TIER_6, 5000, 12f, 0f, 0, () -> Ingredient.of(ModItems.KOBALT.INGOT.get()));


    private final ModTags.Blocks.MinableTag tag;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    ModTiers(ModTags.Blocks.MinableTag tag, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.tag = tag;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        Objects.requireNonNull(repairIngredient);
        this.repairIngredient = repairIngredient;
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.tag.incorrect;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public @Nullable TagKey<Block> getTag() {
        return getTag(this);
    }

    public static TagKey<Block> getTag(ModTiers tier) {
        TagKey<Block> key;
        switch (tier) {
            case INFUSED_ALLOY, KOBALT -> key = BlockTags.NEEDS_IRON_TOOL;
            //add more here
            default -> throw new MatchException(null, null);
        }

        return key;
    }

    public int getIntLevel() {
        return tag.LEVEL;
    }
}
