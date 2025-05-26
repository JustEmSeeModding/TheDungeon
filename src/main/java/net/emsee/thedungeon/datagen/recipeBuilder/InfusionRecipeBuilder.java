package net.emsee.thedungeon.datagen.recipeBuilder;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class InfusionRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final ItemStack resultStack;
    private final ItemLike ingredient;
    private final Map<String, Criterion<?>> criteria;

    public InfusionRecipeBuilder(RecipeCategory category, ItemLike ingredient, ItemLike result, int count) {
        this(category, ingredient, new ItemStack(result, count));
    }

    public InfusionRecipeBuilder(RecipeCategory p_249996_, ItemLike ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.criteria = new LinkedHashMap();
        this.category = p_249996_;
        this.result = result.getItem();
        this.resultStack = result;
    }

    public static InfusionRecipeBuilder infuse(RecipeCategory category, ItemLike ingredient, ItemLike result) {
        return infuse(category, ingredient, result, 1);
    }

    public static InfusionRecipeBuilder infuse(RecipeCategory category, ItemLike ingredient, ItemLike result, int count) {
        return new InfusionRecipeBuilder(category, ingredient, result, count);
    }

    public static InfusionRecipeBuilder infuse(RecipeCategory category, ItemLike ingredient, ItemStack result) {
        return new InfusionRecipeBuilder(category, ingredient, result);
    }

    @Override
    public InfusionRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public InfusionRecipeBuilder group(@Nullable String groupName) {
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
        DungeonInfusionRecipe infusionRecipe = new DungeonInfusionRecipe(Ingredient.of(this.ingredient), this.resultStack);
        recipeOutput.accept(id, infusionRecipe, advancementBuilder.build(id.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        save(recipeOutput, TheDungeon.resourceLocation(getItemName(result) + "_from_infusing_" + getItemName(ingredient)));
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
