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
import net.minecraft.tags.TagKey;
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
    private Ingredient ingredientOne = null;
    private Ingredient ingredientTwo = null;
    private final Map<String, Criterion<?>> criteria;

    public GoblinForgeRecipeBuilder(RecipeCategory category, ItemLike result, int count) {
        this(category, new ItemStack(result, count));
    }

    public GoblinForgeRecipeBuilder(RecipeCategory recipeCategory, ItemStack result) {
        this.criteria = new LinkedHashMap<>();
        this.category = recipeCategory;
        this.result = result.getItem();
        this.resultStack = result;
    }

    public static GoblinForgeRecipeBuilder goblin_forge(RecipeCategory category, ItemLike result) {
        return goblin_forge(category, result, 1);
    }

    public static GoblinForgeRecipeBuilder goblin_forge(RecipeCategory category, ItemLike result, int count) {
        return new GoblinForgeRecipeBuilder(category, result, count);
    }

    public static GoblinForgeRecipeBuilder goblin_forge(RecipeCategory category, ItemStack result) {
        return new GoblinForgeRecipeBuilder(category, result);
    }

    public GoblinForgeRecipeBuilder addIngredient(ItemLike item) {
        return addIngredient(Ingredient.of(item));
    }

    public GoblinForgeRecipeBuilder addIngredient(ItemLike... items) {
        return addIngredient(Ingredient.of(items));
    }
    public GoblinForgeRecipeBuilder addIngredient(TagKey<Item> itemTag) {
        return addIngredient(Ingredient.of(itemTag));
    }


    public GoblinForgeRecipeBuilder addIngredient(Ingredient ingredient) {
        if (ingredientOne == null) {
            ingredientOne = ingredient;
        } else if (ingredientTwo == null) {
            ingredientTwo = ingredient;
        } else throw new IllegalStateException("Can't add three ingredients");
        return this;
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
        if (ingredientOne == null || ingredientTwo == null) throw new IllegalStateException("Needs two ingredients");
        this.ensureValid(id);
        Advancement.Builder advancementBuilder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
        Objects.requireNonNull(advancementBuilder);
        for (String key : this.criteria.keySet()) {
            advancementBuilder.addCriterion(key, this.criteria.get(key));
        }
        GoblinForgeRecipe forgeRecipe = new GoblinForgeRecipe(this.ingredientOne, this.ingredientTwo, this.resultStack);
        recipeOutput.accept(id, forgeRecipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, TheDungeon.defaultResourceLocation(getItemName(result) + "_from_goblin_forge"));
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
