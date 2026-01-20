package net.emsee.thedungeon.datagen.recipeBuilder;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.recipe.GoblinForgeRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * custom DataGen for custom recipes
 */
public class GoblinForgeRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final ItemStack resultStack;
    private final ItemLike ingredientOne;
    private final ItemLike ingredientTwo;
    private final Map<String, Criterion<?>> criteria;

    public GoblinForgeRecipeBuilder(RecipeCategory category, ItemLike ingredientOne, ItemLike ingredientTwo, ItemLike result, int count) {
        this(category, ingredientOne, ingredientTwo, new ItemStack(result, count));
    }

    public GoblinForgeRecipeBuilder(RecipeCategory p_249996_, ItemLike ingredientOne, ItemLike ingredientTwo, ItemStack result) {
        this.ingredientOne = ingredientOne;
        this.ingredientTwo = ingredientTwo;
        this.criteria = new LinkedHashMap<>();
        this.category = p_249996_;
        this.result = result.getItem();
        this.resultStack = result;
    }

    public static GoblinForgeRecipeBuilder goblin_forge(RecipeCategory category, ItemLike ingredientOne, ItemLike ingredientTwo, ItemLike result) {
        return goblin_forge(category, ingredientOne, ingredientTwo, result, 1);
    }

    public static GoblinForgeRecipeBuilder goblin_forge(RecipeCategory category, ItemLike ingredientOne, ItemLike ingredientTwo, ItemLike result, int count) {
        return new GoblinForgeRecipeBuilder(category, ingredientOne, ingredientTwo, result, count);
    }

    public static GoblinForgeRecipeBuilder goblin_forge(RecipeCategory category, ItemLike ingredientOne, ItemLike ingredientTwo, ItemStack result) {
        return new GoblinForgeRecipeBuilder(category, ingredientOne, ingredientTwo, result);
    }

    @Override
    public GoblinForgeRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public GoblinForgeRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    /*public InfusionRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }*/

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        this.ensureValid(id);
        Advancement.Builder advancementBuilder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        Objects.requireNonNull(advancementBuilder);
        for (String key : this.criteria.keySet()) {
            advancementBuilder.addCriterion(key, this.criteria.get(key));
        }
        GoblinForgeRecipe forgeRecipe = new GoblinForgeRecipe(Ingredient.of(this.ingredientOne), Ingredient.of(this.ingredientTwo), this.resultStack);
        recipeOutput.accept(id, forgeRecipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, TheDungeon.defaultResourceLocation(getItemName(result) + "_from_goblin_forge_" + getItemName(ingredientOne) + "_" + getItemName(ingredientTwo)));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id));
        }
    }

    protected static String getItemName(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }
}
