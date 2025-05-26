package net.emsee.thedungeon.block.entity;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<BlockEntityType<DungeonPortalBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity", () -> BlockEntityType.Builder.of(
                    DungeonPortalBlockEntity::new, ModBlocks.DUNGEON_PORTAL.get()).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
