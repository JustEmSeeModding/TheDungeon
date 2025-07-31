package net.emsee.thedungeon.item;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.custom.*;
import net.emsee.thedungeon.item.custom.armor.DungeonScholarArmorItem;
import net.emsee.thedungeon.item.custom.armor.InfusedAlloyArmorItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TheDungeon.MOD_ID);

    public static final DeferredItem<DungeonDebugTool> DUNGEON_DEBUG_TOOL = ITEMS.register("dungeon_debug_tool",
            () -> new DungeonDebugTool(new Item.Properties()));

    public static final DeferredItem<DungeonItem> SHATTERED_PORTAL_CORE = ITEMS.register("shattered_portal_core",
            () -> new DungeonItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<DungeonItem> PORTAL_CORE = ITEMS.register("portal_core",
            () -> new DungeonItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<DungeonItem> DUNGEON_ESSENCE_SHARD = ITEMS.register("dungeon_essence_shard",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> INFUSED_ALLOY_INGOT = ITEMS.register("infused_alloy_ingot",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> PYRITE = ITEMS.register("pyrite",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonClock> DUNGEON_CLOCK = ITEMS.register("dungeon_clock",
            () -> new DungeonClock(new Item.Properties()));

    public static final DeferredItem<DungeonPortalCompas> PYRITE_COMPASS = ITEMS.register("pyrite_compass",
            () -> new DungeonPortalCompas(new Item.Properties()));

    public static final DeferredItem<TestBeltItem> TEST_BELT = ITEMS.register("test_belt",
            () -> new TestBeltItem(new Item.Properties()));

    public static final DeferredItem<DungeonWeaponItem> INFUSED_DAGGER = ITEMS.register("infused_dagger",
            () -> new DungeonWeaponItem(DungeonWeaponItem.WeaponType.SINGLE_HANDED,ModTiers.INFUSED_ALLOY, new Item.Properties().attributes(DungeonWeaponItem.createAttributes(ModTiers.INFUSED_ALLOY, 1, -2F))));

    public static final DeferredItem<DungeonPickaxeItem> INFUSED_CHISEL = ITEMS.register("infused_chisel",
            () ->new DungeonPickaxeItem(ModTiers.INFUSED_ALLOY, new Item.Properties().attributes(DungeonPickaxeItem.createAttributes(ModTiers.INFUSED_ALLOY, 0, -2f))));

    public static final DeferredItem<TestDummyItem> TEST_DUMMY = ITEMS.register("test_dummy",
            () -> new TestDummyItem((new Item.Properties())));

    public static final DeferredItem<DungeonItem> GOBLIN_MEAT = ITEMS.register("goblin_meat",
            () -> new DungeonItem((new Item.Properties().food(new FoodProperties(2,1,false, 2, Optional.empty(), List.of())))));


    public static final DeferredItem<InfusedAlloyArmorItem> INFUSED_ALLOY_HELMET = ITEMS.register("infused_alloy_helmet",
            () -> new InfusedAlloyArmorItem(ModArmorMaterials.INFUSED_ALLOY, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(30))));
    public static final DeferredItem<InfusedAlloyArmorItem> INFUSED_ALLOY_CHESTPLATE = ITEMS.register("infused_alloy_chestplate",
            () -> new InfusedAlloyArmorItem(ModArmorMaterials.INFUSED_ALLOY, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(30))));
    public static final DeferredItem<InfusedAlloyArmorItem> INFUSED_ALLOY_LEGGINGS = ITEMS.register("infused_alloy_leggings",
            () -> new InfusedAlloyArmorItem(ModArmorMaterials.INFUSED_ALLOY, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(30))));
    public static final DeferredItem<InfusedAlloyArmorItem> INFUSED_ALLOY_BOOTS = ITEMS.register("infused_alloy_boots",
            () -> new InfusedAlloyArmorItem(ModArmorMaterials.INFUSED_ALLOY, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(30))));

    public static final DeferredItem<DungeonScholarArmorItem> SCHOLAR_HELMET = ITEMS.register("scholar_helmet",
            () -> new DungeonScholarArmorItem(ModArmorMaterials.DUNGEON_SCHOLAR, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(10))));
    public static final DeferredItem<DungeonScholarArmorItem> SCHOLAR_CHESTPLATE = ITEMS.register("scholar_chestplate",
            () -> new DungeonScholarArmorItem(ModArmorMaterials.DUNGEON_SCHOLAR, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(10))));
    public static final DeferredItem<DungeonScholarArmorItem> SCHOLAR_LEGGINGS = ITEMS.register("scholar_leggings",
            () -> new DungeonScholarArmorItem(ModArmorMaterials.DUNGEON_SCHOLAR, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(10))));
    public static final DeferredItem<DungeonScholarArmorItem> SCHOLAR_BOOTS = ITEMS.register("scholar_boots",
            () -> new DungeonScholarArmorItem(ModArmorMaterials.DUNGEON_SCHOLAR, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(10))));

    /****
     * Registers all custom items and spawn eggs with the provided event bus.
     *
     * @param eventBus the event bus to register items and spawn eggs with
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        ModSpawnEggs.register(eventBus);
    }
}
