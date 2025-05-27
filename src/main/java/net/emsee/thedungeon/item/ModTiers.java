package net.emsee.thedungeon.item;

import com.google.common.base.Suppliers;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public enum ModTiers implements Tier {
    INFUSED_ALLOY(BlockTags.INCORRECT_FOR_IRON_TOOL, 300, 4.0F, 1.0F, 13, () -> Ingredient.of(ModItems.INFUSED_ALLOY_INGOT.get()));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    private ModTiers(TagKey incorrectBlockForDrops, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlockForDrops;
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
        return this.incorrectBlocksForDrops;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient) this.repairIngredient.get();
    }

    public @Nullable TagKey<Block> getTag() {
        return getTag(this);
    }

    public static TagKey<Block> getTag(ModTiers tier) {
        TagKey key;
        switch (tier) {
            case INFUSED_ALLOY -> key = BlockTags.NEEDS_IRON_TOOL;
            default -> throw new MatchException((String)null, (Throwable)null);
        }

        return key;
    }
}
