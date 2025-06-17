package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.datagen.recipeBuilder.InfusionRecipeBuilder;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public final class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        infusing(RecipeCategory.MISC, Blocks.DIRT, ModBlocks.INFUSED_DIRT, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.GRASS_BLOCK, ModBlocks.INFUSED_DIRT, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.CLAY, ModBlocks.INFUSED_CLAY, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.SAND, ModBlocks.INFUSED_SAND, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.GRAVEL, ModBlocks.INFUSED_GRAVEL, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.STONE, ModBlocks.INFUSED_STONE, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.DEEPSLATE, ModBlocks.INFUSED_DEEPSLATE, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.STONE_BRICKS, ModBlocks.INFUSED_STONE_BRICKS, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.NETHERRACK, ModBlocks.INFUSED_NETHERRACK, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.SOUL_SAND, ModBlocks.INFUSED_SOUL_SAND, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.SOUL_SOIL, ModBlocks.INFUSED_SOUL_SOIL, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.END_STONE, ModBlocks.INFUSED_END_STONE, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.END_STONE_BRICKS, ModBlocks.INFUSED_END_STONE_BRICKS, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.GLASS, ModBlocks.INFUSED_GLASS, recipeOutput);
        infusing(RecipeCategory.MISC, Items.STRING, ModBlocks.INFUSED_THREAD, recipeOutput);
        infusing(RecipeCategory.MISC, Items.IRON_INGOT, ModItems.INFUSED_ALLOY_INGOT, recipeOutput);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PatchouliAPI.get().getBookStack(TheDungeon.defaultResourceLocation("dungeon_guide")))
                .requires(Items.BOOK)
                .requires(ModItems.DUNGEON_ESSENCE_SHARD)
                .unlockedBy("has_dungeon_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.INFUSED_DAGGER.get(), 1)
                .pattern("*")
                .pattern("/")
                .define('*', ModItems.INFUSED_ALLOY_INGOT.get())
                .define('/', Items.STICK)
                .unlockedBy("has_infused_alloy", has(ModItems.INFUSED_ALLOY_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.INFUSED_CHISEL.get(), 1)
                .pattern(" *")
                .pattern("/ ")
                .define('*', ModItems.INFUSED_ALLOY_INGOT.get())
                .define('/', Items.STICK)
                .unlockedBy("has_infused_alloy", has(ModItems.INFUSED_ALLOY_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PORTAL_CORE.get(), 1)
                .pattern("^o^")
                .pattern("#*#")
                .pattern("^o^")
                .define('*', ModItems.SHATTERED_PORTAL_CORE.get())
                .define('^', ModItems.DUNGEON_ESSENCE_SHARD.get())
                .define('#', ModItems.INFUSED_ALLOY_INGOT.get())
                .define('o', Items.ENDER_EYE)
                .unlockedBy("has_shattered_portal_core", has(ModItems.SHATTERED_PORTAL_CORE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.DUNGEON_PORTAL_F.get(), 1)
                .pattern("###")
                .pattern("#*#")
                .pattern("###")
                .define('*', ModItems.PORTAL_CORE.get())
                .define('#', Blocks.STONE_BRICKS)
                .unlockedBy("has_portal_core", has(ModItems.PORTAL_CORE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INFUSED_ALLOY_INGOT.get(), 1)
                .requires(ModItems.DUNGEON_ESSENCE_SHARD, 2)
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_dungeon_essence_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput);

        /*oreSmeltingAndBlasting(recipeOutput,
                List.of(ModBlocks.INFUSED_STONE, ModBlocks.INFUSED_DEEPSLATE, ModBlocks.INFUSED_DIRT, ModBlocks.INFUSED_STONE_BRICKS, ModBlocks.INFUSED_GRAVEL, ModBlocks.INFUSED_CLAY, ModBlocks.INFUSED_NETHERRACK, ModBlocks.INFUSED_SOUL_SAND, ModBlocks.INFUSED_SOUL_SOIL),
                RecipeCategory.MISC, ModItems.DUNGEON_ESSENCE_SHARD, 0.25f, 150, 75, "dungeon_essence_shard");
        */
        oreSmeltingAndBlasting(recipeOutput,
                List.of(ModBlocks.INFUSED_SAND),
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.INFUSED_GLASS, 0.25f, 200, 100, "infused_glass");

        oreSmeltingAndBlasting(recipeOutput,
                List.of(ModBlocks.PYRITE_ORE),
                RecipeCategory.BUILDING_BLOCKS, ModItems.PYRITE, 0.25f, 200, 100, "fools_pyrite");

        /*oreBlasting(recipeOutput,
                List.of(ModItems.DUNGEON_DEBUG_TOOL, Items.DIAMOND_AXE),
                RecipeCategory.MISC, ModBlocks.DUNGEON_PORTAL.get(), 0.25f, 100, "portal");*/

        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.CLOCK, ModItems.INFUSED_ALLOY_INGOT, ModItems.DUNGEON_CLOCK.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_HELMET, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ALLOY_HELMET.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_CHESTPLATE, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ALLOY_CHESTPLATE.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_LEGGINGS, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ALLOY_LEGGINGS.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_BOOTS, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ALLOY_BOOTS.get(), RecipeCategory.COMBAT, recipeOutput);
    }

    private static void infusing(RecipeCategory category, ItemLike ingredient, ItemLike result, RecipeOutput recipeOutput) {
        InfusionRecipeBuilder.infuse(category, ingredient, result)
                .unlockedBy("has_"+getItemName(ingredient), has(ingredient)).save(recipeOutput);

    }

    private static void smithing(ItemLike template, ItemLike item, ItemLike ingredient, Item resultItem, RecipeCategory category, RecipeOutput recipeOutput) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(item), Ingredient.of(ingredient), category, resultItem)
                .unlocks("has_"+getItemName(ingredient), has(ingredient)).save(recipeOutput, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID,getItemName(resultItem) + "_smithing"));

    }

    private static void oreSmeltingAndBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, int blastingTime, String group) {
        oreSmelting(recipeOutput, ingredients, category, result, experience, cookingTime, group);
        oreBlasting(recipeOutput, ingredients, category, result, experience, blastingTime, group);
    }

    private static void oreSmeltingAndSmoking(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, int smokingTime, String group) {
        oreSmelting(recipeOutput, ingredients, category, result, experience, cookingTime, group);
        oreSmoking(recipeOutput, ingredients, category, result, experience, smokingTime, group);
    }

    private static void oreSmeltingAndSmokingAndCampfire(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, int smokingTime, int campfireTime, String group) {
        oreSmelting(recipeOutput, ingredients, category, result, experience, cookingTime, group);
        oreSmoking(recipeOutput, ingredients, category, result, experience, smokingTime, group);
        oreCampfire(recipeOutput, ingredients, category, result, experience, campfireTime, group);
    }

    protected static void oreSmelting(@NotNull RecipeOutput recipeOutput, List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult, float pExperience, int pCookingTime, @NotNull String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(@NotNull RecipeOutput recipeOutput, List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult, float pExperience, int pCookingTime, @NotNull String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreSmoking(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static void oreCampfire(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_campfire");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(@NotNull RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.@NotNull Factory<T> factory,
                                                                       List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult, float pExperience, int pCookingTime, @NotNull String pGroup, String pRecipeName) {
        for (ItemLike itemLike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemLike), has(itemLike))
                    .save(recipeOutput, TheDungeon.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemLike));
        }

    }
}
