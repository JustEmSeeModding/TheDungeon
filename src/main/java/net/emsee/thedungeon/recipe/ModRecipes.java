package net.emsee.thedungeon.recipe;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, TheDungeon.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, TheDungeon.MOD_ID);


    public static final Supplier<RecipeSerializer<DungeonInfusionRecipe>> DUNGEON_INFUSION_SERIALIZER =
            SERIALIZERS.register("dungeon_infusion", DungeonInfusionRecipe.Serializer::new);
    public static final Supplier<RecipeType<DungeonInfusionRecipe>> DUNGEON_INFUSION_TYPE =
            TYPES.register("dungeon_infusion", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "dungeon_infusion";
                }
            });


    public static final Supplier<RecipeSerializer<GoblinForgeRecipe>> GOBLIN_FORGE_SERIALIZER =
            SERIALIZERS.register("goblin_forge", GoblinForgeRecipe.Serializer::new);
    public static final Supplier<RecipeType<GoblinForgeRecipe>> GOBLIN_FORGE_TYPE =
            TYPES.register("goblin_forge", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "goblin_forge";
                }
            });


    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
