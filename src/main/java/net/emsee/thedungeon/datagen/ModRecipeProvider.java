package net.emsee.thedungeon.datagen;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.datagen.recipeBuilder.GoblinForgeRecipeBuilder;
import net.emsee.thedungeon.datagen.recipeBuilder.InfusionRecipeBuilder;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleBlockGroup;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleItemGroup;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public final class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        infusing(RecipeCategory.MISC, Blocks.DIRT, ModBlocks.INFUSED_DIRT, recipeOutput);
        infusing(RecipeCategory.MISC, Blocks.GRASS_BLOCK, ModBlocks.INFUSED_GRASS_BLOCK, recipeOutput);
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

        infusing(RecipeCategory.MISC, Items.CLOCK, ModItems.DUNGEON_CLOCK, recipeOutput);


        GoblinForgeRecipeBuilder.goblin_forge(RecipeCategory.MISC, ModItems.KOBALT.INGOT)
                .addIngredient(ModItems.LAVINTINE.INGOT, ModItems.LAVINTINE.RAW)
                .addIngredient(ModItems.INFERNAL_TIN.INGOT, ModItems.INFERNAL_TIN.RAW)
                .unlockedBy("has_"+getItemName(ModItems.INFERNAL_TIN.INGOT), has(ModItems.INFERNAL_TIN.INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GILDREAN_APPLE.get(), 1)
                .pattern("000")
                .pattern("0*0")
                .pattern("000")
                .define('0', ModItems.GILDREAN.INGOT.get())
                .define('*', Items.APPLE)
                .unlockedBy("has_gildrean_ingot", has(ModItems.GILDREAN.INGOT)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PatchouliAPI.get().getBookStack(TheDungeon.defaultResourceLocation("dungeon_guide")))
                .requires(Items.BOOK)
                .requires(ModItems.DUNGEON_ESSENCE_SHARD)
                .unlockedBy("has_dungeon_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SOUL_BOUND_TOTEM.get(), 1)
                .pattern("0u0")
                .pattern("0*0")
                .pattern("0u0")
                .define('u', Items.SHULKER_SHELL)
                .define('0', ModItems.PYRITE.get())
                .define('*', ModItems.VERDANTITE_CHUNK.get())
                .unlockedBy("has_infused_alloy", has(ModItems.INFUSED_ALLOY_INGOT)).save(recipeOutput);

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CATALYST_CORE.get(), 1)
                .pattern("^o^")
                .pattern("#*#")
                .pattern("^o^")
                .define('*', ModItems.SHATTERED_CATALYST_CORE.get())
                .define('^', ModItems.DUNGEON_ESSENCE_SHARD.get())
                .define('#', ModItems.INFUSED_ALLOY_INGOT.get())
                .define('o', Items.ENDER_EYE)
                .unlockedBy("has_shattered_catalist_core", has(ModItems.SHATTERED_CATALYST_CORE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DUNGEON_CLOCK.get(), 1)
                .pattern(" # ")
                .pattern("#*#")
                .pattern(" # ")
                .define('*', Items.CLOCK)
                .define('#', ModItems.DUNGEON_ESSENCE_SHARD.get())
                .unlockedBy("has_essence_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PYRITE_COMPASS.get(), 1)
                .pattern(" # ")
                .pattern("#*#")
                .pattern(" # ")
                .define('*', Items.COMPASS)
                .define('#', ModItems.PYRITE.get())
                .unlockedBy("has_pyrite", has(ModItems.PYRITE)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CATALYST_F.get(), 1)
                .pattern("+#+")
                .pattern("#*#")
                .pattern("+#+")
                .define('*', ModItems.CATALYST_CORE.get())
                .define('#', Blocks.STONE_BRICKS)
                .define('+', Items.IRON_INGOT)
                .unlockedBy("has_catalist_core", has(ModItems.CATALYST_CORE)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INFUSED_ALLOY_INGOT.get(), 1)
                .requires(ModItems.DUNGEON_ESSENCE_SHARD, 2)
                .requires(Items.IRON_INGOT)
                .unlockedBy("has_dungeon_essence_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput);

        simpleArmor(ModItems.KOBALT.INGOT, ModItems.KOBALT_ARMOR_SET, recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.KOBALT_SHIELD, 1)
                .pattern(" # ")
                .pattern("#*#")
                .pattern(" # ")
                .define('*', ItemTags.PLANKS)
                .define('#', ModItems.KOBALT.INGOT)
                .unlockedBy("has_" + getItemName(ModItems.KOBALT.INGOT), has(ModItems.KOBALT.INGOT)).save(recipeOutput);


        oreSmeltingAndBlasting(recipeOutput,
                List.of(ModBlocks.INFUSED_SAND),
                RecipeCategory.BUILDING_BLOCKS, ModBlocks.INFUSED_GLASS, 0.25f, 200, 100, "infused_glass");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.INFUSED_WHEAT.get(), 1)
                .requires(ModItems.DUNGEON_ESSENCE_SHARD, 1)
                .requires(Items.WHEAT)
                .unlockedBy("has_dungeon_essence_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.INFUSED_BREAD.get(), 1)
                .requires(ModItems.DUNGEON_ESSENCE_SHARD, 3)
                .requires(Items.BREAD)
                .unlockedBy("has_dungeon_essence_shard", has(ModItems.DUNGEON_ESSENCE_SHARD)).save(recipeOutput, TheDungeon.defaultResourceLocation(getItemName(ModItems.INFUSED_BREAD) + "alt"));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.INFUSED_BREAD, 1)
                .pattern("###")
                .define('#', ModItems.INFUSED_WHEAT)
                .unlockedBy("has_" + getItemName(ModItems.INFUSED_WHEAT), has(ModItems.INFUSED_WHEAT)).save(recipeOutput);

        infusing(RecipeCategory.MISC, Items.WHEAT, ModItems.INFUSED_WHEAT, recipeOutput);
        infusing(RecipeCategory.FOOD, Items.BREAD, ModItems.INFUSED_BREAD, recipeOutput);

        simpleBlockAndOreItemRecipes(recipeOutput, ModBlocks.PYRITE_BLOCKS, ModItems.PYRITE.get());
        simpleBlockAndOreIngotRecipes(recipeOutput, ModBlocks.GILDREAN, ModItems.GILDREAN);
        simpleBlockAndOreIngotRecipes(recipeOutput, ModBlocks.INFERNAL_TIN, ModItems.INFERNAL_TIN);
        simpleBlockAndOreIngotRecipes(recipeOutput, ModBlocks.ARCTIC_IRON, ModItems.ARCTIC_IRON);
        simpleBlockAndOreIngotRecipes(recipeOutput, ModBlocks.LAVINTINE, ModItems.LAVINTINE);
        packedItemCrafting(recipeOutput, ModBlocks.KOBALT_BLOCK.get(), ModItems.KOBALT.INGOT.get(), RecipeCategory.BUILDING_BLOCKS, RecipeCategory.MISC);
        simpleIngotAndNuggetRecipes(recipeOutput, ModItems.KOBALT);

        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_HELMET, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ARMOR_SET.HELMET.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_CHESTPLATE, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ARMOR_SET.CHESTPLATE.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_LEGGINGS, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ARMOR_SET.LEGGINGS.get(), RecipeCategory.COMBAT, recipeOutput);
        smithing(ModItems.DUNGEON_ESSENCE_SHARD, Items.IRON_BOOTS, ModItems.INFUSED_ALLOY_INGOT, ModItems.INFUSED_ARMOR_SET.BOOTS.get(), RecipeCategory.COMBAT, recipeOutput);
    }

    private static void infusing(RecipeCategory category, ItemLike ingredient, ItemLike result, RecipeOutput recipeOutput) {
        InfusionRecipeBuilder.infuse(category, ingredient, result)
                .unlockedBy("has_" + getItemName(ingredient), has(ingredient)).save(recipeOutput);

    }


    private static void smithing(ItemLike template, ItemLike item, ItemLike ingredient, Item resultItem, RecipeCategory category, RecipeOutput recipeOutput) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(template), Ingredient.of(item), Ingredient.of(ingredient), category, resultItem)
                .unlocks("has_" + getItemName(ingredient), has(ingredient)).save(recipeOutput, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, getItemName(resultItem) + "_smithing"));

    }

    private static void oreSmeltingAndBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, int blastingTime, String group) {
        oreSmelting(recipeOutput, ingredients, category, result, experience, cookingTime, group);
        oreBlasting(recipeOutput, ingredients, category, result, experience, blastingTime, group);
    }

    private static <I extends SimpleItemGroup.WithIngot, BG extends SimpleBlockGroup & SimpleBlockGroup.WithOres> void simpleBlockAndOreIngotRecipes(RecipeOutput recipeOutput, BG blockGroup, I items) {
        List<ItemLike> Ingredient = new ArrayList<>(blockGroup.getAllOresAsItems());
        if (items instanceof SimpleItemGroup.WithRaw withRaw)
            Ingredient.add(withRaw.getRaw());

        oreSmeltingAndBlasting(recipeOutput, Ingredient, RecipeCategory.MISC, items.getIngot(), 0.25f, 200, 100, getItemName(items.getIngot()));

        if (blockGroup instanceof SimpleBlockGroup.WithPackedItemBlock withPackedBlock) {
            packedItemCrafting(recipeOutput, withPackedBlock.getPackedItemBlock(), items.getIngot(), RecipeCategory.BUILDING_BLOCKS, RecipeCategory.MISC);
        }
        if (blockGroup instanceof SimpleBlockGroup.WithRawBlock withRawBlock && items instanceof SimpleItemGroup.WithRaw withRawItem) {
            packedItemCrafting(recipeOutput, withRawBlock.getRawBlock(), withRawItem.getRaw(), RecipeCategory.BUILDING_BLOCKS, RecipeCategory.MISC);
        }
        if (items instanceof SimpleItemGroup.WithNugget) {
            simpleIngotAndNuggetRecipes(recipeOutput, (SimpleItemGroup & SimpleItemGroup.WithIngot & SimpleItemGroup.WithNugget) items);
        }
    }

    private static <BG extends SimpleBlockGroup & SimpleBlockGroup.WithOres> void simpleBlockAndOreItemRecipes(RecipeOutput recipeOutput, BG blockGroup, Item linkedItem) {
        oreSmeltingAndBlasting(recipeOutput, blockGroup.getAllOresAsItems(), RecipeCategory.MISC, linkedItem, 0.25f, 200, 100, getItemName(linkedItem));
        if (blockGroup instanceof SimpleBlockGroup.WithPackedItemBlock withPackedItem) {
            packedItemCrafting(recipeOutput, withPackedItem.getPackedItemBlock(), linkedItem, RecipeCategory.BUILDING_BLOCKS, RecipeCategory.MISC);
        }
    }

    private static <I extends SimpleItemGroup & SimpleItemGroup.WithIngot & SimpleItemGroup.WithNugget> void simpleIngotAndNuggetRecipes(RecipeOutput recipeOutput, I itemGroup) {
        packedItemCrafting(recipeOutput, itemGroup.getIngot(), itemGroup.getNugget(), RecipeCategory.MISC, RecipeCategory.MISC);
    }

    private static void packedItemCrafting(RecipeOutput recipeOutput, ItemLike packed, ItemLike unPacked, RecipeCategory packedCategory, RecipeCategory unPackedCategory) {
        ShapedRecipeBuilder.shaped(packedCategory, packed, 1)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', unPacked)
                .unlockedBy("has_" + getItemName(unPacked), has(unPacked)).save(recipeOutput, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, getItemName(packed) + "_from_" + getItemName(unPacked)));
        ShapelessRecipeBuilder.shapeless(unPackedCategory, unPacked, 9)
                .requires(packed)
                .unlockedBy("has_" + getItemName(packed), has(packed)).save(recipeOutput, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, getItemName(unPacked) + "_from_" + getItemName(packed)));


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

    private static void oreSmoking(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    private static void oreCampfire(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_campfire");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(@NotNull RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.@NotNull Factory<T> factory, List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory, @NotNull ItemLike pResult, float pExperience, int pCookingTime, @NotNull String pGroup, String pRecipeName) {
        for (ItemLike itemLike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemLike), has(itemLike))
                    .save(recipeOutput, TheDungeon.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemLike));
        }
    }

    private static void simpleArmor(ItemLike ingredient, SimpleItemGroup.ArmorSet<?> armorGroup, RecipeOutput recipeOutput) {
        simpleArmor(ingredient, armorGroup.HELMET, armorGroup.CHESTPLATE, armorGroup.LEGGINGS, armorGroup.BOOTS, recipeOutput);
    }

    private static void simpleArmor(ItemLike ingredient, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots, RecipeOutput recipeOutput) {
        if (helmet != null)
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, helmet, 1)
                    .pattern("###")
                    .pattern("# #")
                    .define('#', ingredient)
                    .unlockedBy("has_" + getItemName(ingredient), has(ingredient)).save(recipeOutput);
        if (chestplate != null)
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, chestplate, 1)
                    .pattern("# #")
                    .pattern("###")
                    .pattern("###")
                    .define('#', ingredient)
                    .unlockedBy("has_" + getItemName(ingredient), has(ingredient)).save(recipeOutput);
        if (leggings != null)
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, leggings, 1)
                    .pattern("###")
                    .pattern("# #")
                    .pattern("# #")
                    .define('#', ingredient)
                    .unlockedBy("has_" + getItemName(ingredient), has(ingredient)).save(recipeOutput);
        if (boots != null)
            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, boots, 1)
                    .pattern("# #")
                    .pattern("# #")
                    .define('#', ingredient)
                    .unlockedBy("has_" + getItemName(ingredient), has(ingredient)).save(recipeOutput);
    }
}
