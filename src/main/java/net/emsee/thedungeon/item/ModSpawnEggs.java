package net.emsee.thedungeon.item;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSpawnEggs {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TheDungeon.MOD_ID);

    public static final DeferredItem<Item> DEATH_KNIGHT_SPAWN_EGG = ITEMS.register("death_knight_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.DEATH_KNIGHT, 0x301D1D, 0x100909,
                    new Item.Properties()));

    public static final DeferredItem<Item> SKELETON_KNIGHT_SPAWN_EGG = ITEMS.register("skeleton_knight_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SKELETON_KNIGHT, 0xA5A5A5, 0xB1B1B1,
                    new Item.Properties()));

    public static final DeferredItem<Item> CAVE_GOBLIN_SPAWN_EGG = ITEMS.register("cave_goblin_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CAVE_GOBLIN, 0x808080, 0xB1B1B1,
                    new Item.Properties()));

    public static final DeferredItem<Item> SHADOW_GOBLIN_SPAWN_EGG = ITEMS.register("shadow_goblin_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SHADOW_GOBLIN, 0x000000, 0xB1B1B1,
                    new Item.Properties()));

    public static final DeferredItem<Item> HOB_GOBLIN_SPAWN_EGG = ITEMS.register("hob_goblin_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.HOB_GOBLIN, 0x000000, 0x000000,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
