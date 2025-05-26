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
import org.jetbrains.annotations.NotNull;

public record DungeonInfusionRecipe(Ingredient inputItem, ItemStack output) implements Recipe<DungeonInfusionRecipeInput> {
    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(@NotNull DungeonInfusionRecipeInput dungeonInfusionRecipeInput, Level level) {
        if (level.isClientSide) return false;
        return inputItem.test(dungeonInfusionRecipeInput.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull DungeonInfusionRecipeInput dungeonInfusionRecipeInput, HolderLookup.@NotNull Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipes.DUNGEON_INFUSION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipes.DUNGEON_INFUSION_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<DungeonInfusionRecipe> {
        public static final MapCodec<DungeonInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(DungeonInfusionRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(DungeonInfusionRecipe::output)
        ).apply(inst, DungeonInfusionRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DungeonInfusionRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, DungeonInfusionRecipe::inputItem,
                        ItemStack.STREAM_CODEC, DungeonInfusionRecipe::output,
                        DungeonInfusionRecipe::new);

        @Override
        public @NotNull MapCodec<DungeonInfusionRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DungeonInfusionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
