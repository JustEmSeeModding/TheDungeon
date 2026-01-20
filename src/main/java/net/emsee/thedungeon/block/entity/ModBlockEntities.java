package net.emsee.thedungeon.block.entity;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.custom.GoblinForgeBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<BlockEntityType<GoblinForgeBlockEntity>> GOBLIN_FORGE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("goblin_forge_block_entity", () -> BlockEntityType.Builder.of(
                    GoblinForgeBlockEntity::new, ModBlocks.GOBLIN_FORGE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
