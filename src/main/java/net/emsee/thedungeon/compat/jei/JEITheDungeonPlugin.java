package net.emsee.thedungeon.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipe;
import net.emsee.thedungeon.recipe.ModRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public final class JEITheDungeonPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DungeonInWorldInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new DungeonInfusionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<DungeonInfusionRecipe> dungeonInfusionRecipes = new ArrayList<>(recipeManager
                .getAllRecipesFor(ModRecipes.DUNGEON_INFUSION_TYPE.get()).stream().map(RecipeHolder::value).toList());
        List<DungeonInfusionRecipe> blockDungeonInfusionRecipes = new ArrayList<>();
        for (DungeonInfusionRecipe recipe : dungeonInfusionRecipes)
            if (recipe.inputItem().getItems()[0].getItem() instanceof BlockItem)
                blockDungeonInfusionRecipes.add(recipe);

        registration.addRecipes(DungeonInWorldInfusionRecipeCategory.IN_WORLD_DUNGEON_INFUSION_RECIPE_RECIPE_TYPE, blockDungeonInfusionRecipes);
        registration.addRecipes(DungeonInfusionRecipeCategory.DUNGEON_INFUSION_RECIPE_RECIPE_TYPE, dungeonInfusionRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.DUNGEON_PORTAL_UNSTABLE, DungeonInWorldInfusionRecipeCategory.IN_WORLD_DUNGEON_INFUSION_RECIPE_RECIPE_TYPE);
        registration.addRecipeCatalyst(Blocks.DIRT, DungeonInfusionRecipeCategory.DUNGEON_INFUSION_RECIPE_RECIPE_TYPE);
    }
}
