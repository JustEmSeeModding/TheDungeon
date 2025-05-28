package net.emsee.thedungeon.block.entity;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.portal.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<BlockEntityType<DungeonPortalFBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_F =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_f", () -> BlockEntityType.Builder.of(
                    DungeonPortalFBlockEntity::new, ModBlocks.DUNGEON_PORTAL_F.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalEBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_E =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_e", () -> BlockEntityType.Builder.of(
                    DungeonPortalEBlockEntity::new, ModBlocks.DUNGEON_PORTAL_E.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalDBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_D =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_d", () -> BlockEntityType.Builder.of(
                    DungeonPortalDBlockEntity::new, ModBlocks.DUNGEON_PORTAL_D.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalCBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_C =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_c", () -> BlockEntityType.Builder.of(
                    DungeonPortalCBlockEntity::new, ModBlocks.DUNGEON_PORTAL_C.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalBBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_B =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_b", () -> BlockEntityType.Builder.of(
                    DungeonPortalBBlockEntity::new, ModBlocks.DUNGEON_PORTAL_B.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalABlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_A =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_a", () -> BlockEntityType.Builder.of(
                    DungeonPortalABlockEntity::new, ModBlocks.DUNGEON_PORTAL_A.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalSBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_S =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_s", () -> BlockEntityType.Builder.of(
                    DungeonPortalSBlockEntity::new, ModBlocks.DUNGEON_PORTAL_S.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalSSBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_SS =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_ss", () -> BlockEntityType.Builder.of(
                    DungeonPortalSSBlockEntity::new, ModBlocks.DUNGEON_PORTAL_SS.get()).build(null)
            );

    public static final Supplier<BlockEntityType<DungeonPortalExitBlockEntity>> DUDGEON_PORTAL_BLOCKENTITY_EXIT =
            BLOCK_ENTITIES.register("dungeon_portal_blockentity_exit", () -> BlockEntityType.Builder.of(
                    DungeonPortalExitBlockEntity::new, ModBlocks.DUNGEON_PORTAL_EXIT.get()).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
