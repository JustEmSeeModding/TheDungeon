package net.emsee.thedungeon.recipe;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, TheDungeon.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, TheDungeon.MOD_ID);


    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DungeonInfusionRecipe>> DUNGEON_INFUSION_SERIALIZER =
            SERIALIZERS.register("dungeon_infusion", DungeonInfusionRecipe.Serializer::new);
    public static final DeferredHolder<RecipeType<?>, RecipeType<DungeonInfusionRecipe>> DUNGEON_INFUSION_TYPE =
            TYPES.register("dungeon_infusion", () -> new RecipeType</*DungeonInfusionRecipe*/>() {
                @Override
                public String toString() {
                    return "dungeon_infusion";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
