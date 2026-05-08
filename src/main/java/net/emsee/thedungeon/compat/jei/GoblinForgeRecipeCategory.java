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
import net.emsee.thedungeon.recipe.GoblinForgeRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GoblinForgeRecipeCategory implements IRecipeCategory<GoblinForgeRecipe> {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "goblin_forge");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "textures/gui/jei/goblin_forge.png");

    private final IDrawable background;
    private final IDrawable icon;

    public static final RecipeType<GoblinForgeRecipe> GOBLIN_FORGE_RECIPE_TYPE =
            new RecipeType<>(UID, GoblinForgeRecipe.class);

    public GoblinForgeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 179, 74);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.GOBLIN_FORGE));
    }

    @Override
    public RecipeType<GoblinForgeRecipe> getRecipeType() {
        return GOBLIN_FORGE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.thedungeon.goblin_forge_title");
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
    public void setRecipe(IRecipeLayoutBuilder builder, GoblinForgeRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(47,17).addIngredients(recipe.getIngredients().get(0));
        builder.addInputSlot(65,17).addIngredients(recipe.getIngredients().get(1));
        builder.addOutputSlot(116,35).addItemStack(recipe.getResultItem(null));
    }
}
