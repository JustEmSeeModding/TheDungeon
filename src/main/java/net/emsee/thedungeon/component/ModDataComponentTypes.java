package net.emsee.thedungeon.component;

import com.mojang.serialization.Codec;
import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;


public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TheDungeon.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<Integer>> ITEM_SAVED_DUNGEON_ID = register("item_dungeon_id",
            builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>,DataComponentType<BlockPos>> COMPAS_SAVED_BLOCK_POS= register("compas_block_pos",
            builder -> builder.persistent(BlockPos.CODEC));

    private static <T>DeferredHolder<DataComponentType<?>,DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }

}
