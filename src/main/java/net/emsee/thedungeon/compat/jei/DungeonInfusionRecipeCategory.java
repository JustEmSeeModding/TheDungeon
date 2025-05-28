package net.emsee.thedungeon.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.recipe.DungeonInfusionRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class DungeonInfusionRecipeCategory implements IRecipeCategory<DungeonInfusionRecipe> {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "dungeon_infusion");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "textures/gui/jei/dungeon_infusion.png");

    private final IDrawable background;
    private final IDrawable icon;

    public static final RecipeType<DungeonInfusionRecipe> IN_WORLD_DUNGEON_INFUSION_RECIPE_RECIPE_TYPE =
            new RecipeType<>(UID, DungeonInfusionRecipe.class);

    public DungeonInfusionRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 179, 34);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.DUNGEON_PORTAL_UNSTABLE));
    }

    @Override
    public RecipeType<DungeonInfusionRecipe> getRecipeType() {
        return IN_WORLD_DUNGEON_INFUSION_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.thedungeon.infusion_recipe_title");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }


    // IGNORE THIS ERROR FOR NOW
    @Override
    @Nullable
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DungeonInfusionRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(54,10).addIngredients(recipe.getIngredients().getFirst());
        builder.addOutputSlot(103,10).addItemStack(recipe.getResultItem(null));
    }

    /*@Override
    public void draw(DungeonInfusionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        background.draw(guiGraphics);
    }*/
}
