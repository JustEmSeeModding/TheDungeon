package net.emsee.thedungeon.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record GoblinForgeRecipe (Ingredient inputItemOne, Ingredient inputItemTwo, ItemStack output) implements Recipe<GoblinForgeRecipeInput> {
    public static final Boolean ALLOW_SWAP_ITEMS = true;

    public boolean canInputInOne(ItemStack stack) {
        return inputItemOne.test(stack) || (ALLOW_SWAP_ITEMS && inputItemTwo.test(stack));
    }
    public boolean canInputInTwo(ItemStack stack) {
        return inputItemTwo.test(stack) || (ALLOW_SWAP_ITEMS && inputItemOne.test(stack));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItemOne);
        list.add(inputItemTwo);
        return list;
    }

    @Override
    public boolean matches(GoblinForgeRecipeInput input, Level level) {
        if (level.isClientSide) return false;
        boolean matches = inputItemOne.test(input.getItem(0)) && inputItemTwo.test(input.getItem(1));
        boolean matchesInverse = ALLOW_SWAP_ITEMS && inputItemOne.test(input.getItem(1)) && inputItemTwo.test(input.getItem(0));
        return matches || matchesInverse;
    }

    @Override
    public ItemStack assemble(GoblinForgeRecipeInput goblinForgeRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GOBLIN_FORGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.GOBLIN_FORGE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<GoblinForgeRecipe> {
        public static final MapCodec<GoblinForgeRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_one").forGetter(GoblinForgeRecipe::inputItemOne),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient_two").forGetter(GoblinForgeRecipe::inputItemTwo),
                ItemStack.CODEC.fieldOf("result").forGetter(GoblinForgeRecipe::output)
        ).apply(inst, GoblinForgeRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, GoblinForgeRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, GoblinForgeRecipe::inputItemOne,
                        Ingredient.CONTENTS_STREAM_CODEC, GoblinForgeRecipe::inputItemTwo,
                        ItemStack.STREAM_CODEC, GoblinForgeRecipe::output,
                        GoblinForgeRecipe::new);

        @Override
        public MapCodec<GoblinForgeRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GoblinForgeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
