package net.emsee.thedungeon.block.entity;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.entity.custom.DungeonPortalBlockEntity;
import net.emsee.thedungeon.block.entity.custom.fightRoom.AbstractFightRoomBlockEntity;
import net.emsee.thedungeon.block.entity.custom.GoblinForgeBlockEntity;
import net.emsee.thedungeon.block.entity.custom.fightRoom.TestFightRoomBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<BlockEntityType<DungeonPortalBlockEntity>> DUNGEON_PORTAL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("dungeon_portal_block_entity", () -> BlockEntityType.Builder.of(
                    DungeonPortalBlockEntity::new, ModBlocks.DUNGEON_PORTAL.get()).build(null));


    public static final Supplier<BlockEntityType<GoblinForgeBlockEntity>> GOBLIN_FORGE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("goblin_forge_block_entity", () -> BlockEntityType.Builder.of(
                    GoblinForgeBlockEntity::new, ModBlocks.GOBLIN_FORGE.get()).build(null));

    public static final Supplier<BlockEntityType<TestFightRoomBlockEntity>> FIGHT_ROOM_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("fight_room_block_entity", () -> BlockEntityType.Builder.of(
                    TestFightRoomBlockEntity::new, ModBlocks.TEST_FIGHT.get()).build(null));





    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
