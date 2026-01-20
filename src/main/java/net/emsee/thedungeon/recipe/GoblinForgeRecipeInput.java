package net.emsee.thedungeon.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record GoblinForgeRecipeInput (ItemStack inputOne, ItemStack inputTwo) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return switch (i) {
            case 0 -> inputOne;
            case 1 -> inputTwo;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
