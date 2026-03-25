package net.emsee.thedungeon.item;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.ModClasses;
import net.emsee.thedungeon.item.custom.*;
import net.emsee.thedungeon.item.custom.DungeonCurio;
import net.emsee.thedungeon.item.custom.armor.DungeonScholarArmorItem;
import net.emsee.thedungeon.item.custom.armor.InfusedAlloyArmorItem;
import net.emsee.thedungeon.item.custom.armor.KobaltArmorItem;
import net.emsee.thedungeon.item.custom.armor.ArcticIroncladArmorItem;
import net.emsee.thedungeon.simpleRegisterGroup.SimpleItemGroup;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TheDungeon.MOD_ID);

    public static final DeferredItem<DungeonDebugTool> DUNGEON_DEBUG_TOOL = ITEMS.register("dungeon_debug_tool",
            () -> new DungeonDebugTool(new Item.Properties()));

    public static final DeferredItem<DungeonItem> SHATTERED_CATALYST_CORE = ITEMS.register("shattered_catalyst_core",
            () -> new DungeonItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<DungeonItem> CATALYST_CORE = ITEMS.register("catalyst_core",
            () -> new DungeonItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<DungeonItem> DUNGEON_ESSENCE_SHARD = ITEMS.register("dungeon_essence_shard",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> INFUSED_ALLOY_INGOT = ITEMS.register("infused_alloy_ingot",
            () -> new DungeonItem(new Item.Properties()));

    public static final SimpleItemGroup.IngotAndNugget KOBALT = new SimpleItemGroup.IngotAndNugget("kobalt");
    public static final DeferredItem<DungeonItem> PYRITE = ITEMS.register("pyrite",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> PYRITE_COIN = ITEMS.register("pyrite_coin",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> ROSELITH_CRYSTAL = ITEMS.register("roselith_crystal",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> GARNETORE_PIECE = ITEMS.register("garnetore_piece",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> VERDANTITE_CHUNK = ITEMS.register("verdantite_chunk",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonItem> LUMANITE_FRAGMENT = ITEMS.register("lumanite_fragment",
            () -> new DungeonItem(new Item.Properties()));

    public static final DeferredItem<DungeonClock> DUNGEON_CLOCK = ITEMS.register("dungeon_clock",
            () -> new DungeonClock(new Item.Properties()));

    public static final DeferredItem<DungeonPortalCompas> PYRITE_COMPASS = ITEMS.register("pyrite_compass",
            () -> new DungeonPortalCompas(new Item.Properties()));

    public static final DeferredItem<DungeonCurio> TEST_BELT = ITEMS.register("test_belt",
            () -> new DungeonCurio(new Item.Properties(), DungeonItemRank.F, new DeferredHolder[]{}, new DeferredHolder[]{}));

    public static final DeferredItem<SoulBoundTotem> SOUL_BOUND_TOTEM = ITEMS.register("soul_bound_totem",
            () -> new SoulBoundTotem(new Item.Properties(), DungeonItemRank.E, new DeferredHolder[]{}, new DeferredHolder[]{}));


    public static final DeferredItem<DungeonWeaponItem> INFUSED_DAGGER = ITEMS.register("infused_dagger",
            () -> new DungeonWeaponItem(DungeonWeaponItem.WeaponType.SINGLE_HANDED,true,ModTiers.INFUSED_ALLOY, DungeonItemRank.F, new DeferredHolder[]{}, new DeferredHolder[]{}, new Item.Properties().attributes(DungeonWeaponItem.createAttributes(ModTiers.INFUSED_ALLOY, 2, -2F))));

    public static final DeferredItem<DungeonToolItem> INFUSED_CHISEL = ITEMS.register("infused_chisel",
            () ->new DungeonToolItem(DungeonWeaponItem.WeaponType.SINGLE_HANDED, false,ModTiers.INFUSED_ALLOY, DungeonItemRank.F, new DeferredHolder[]{}, new DeferredHolder[]{}, new Item.Properties().attributes(DungeonToolItem.createAttributes(ModTiers.INFUSED_ALLOY, 1, -2f))));

    public static final DeferredItem<DungeonWeaponItem> KOBALT_DAGGER = ITEMS.register("kobalt_dagger",
            () -> new DungeonWeaponItem(DungeonWeaponItem.WeaponType.SINGLE_HANDED, true,ModTiers.KOBALT, DungeonItemRank.F, new DeferredHolder[]{}, new DeferredHolder[]{}, new Item.Properties().attributes(DungeonWeaponItem.createAttributes(ModTiers.KOBALT, 3.5f, -2F))));

    public static final DeferredItem<DungeonToolItem> GOBLINS_FORGEHAMMER = ITEMS.register("goblins_forgehammer",
            () ->new DungeonToolItem(DungeonWeaponItem.WeaponType.SINGLE_HANDED, false,ModTiers.KOBALT, DungeonItemRank.D, new DeferredHolder[]{}, new DeferredHolder[]{}, new Item.Properties().attributes(DungeonToolItem.createAttributes(ModTiers.KOBALT, 8f, -3.2f))));


    public static final DeferredItem<DungeonWeaponItem> ARCTIC_IRON_SWORD = ITEMS.register("arctic_iron_sword",
            () -> new DungeonWeaponItem(DungeonWeaponItem.WeaponType.SINGLE_HANDED, true,ModTiers.ARCTIC_IRON, DungeonItemRank.F, new DeferredHolder[]{}, new DeferredHolder[]{}, new Item.Properties().attributes(DungeonWeaponItem.createAttributes(ModTiers.ARCTIC_IRON, 3.5f, -2F))));

    public static final DeferredItem<DungeonShieldItem> KOBALT_SHIELD = ITEMS.register("kobalt_shield",
            () ->new DungeonShieldItem(new Item.Properties().durability(400), 2, 0, DungeonItemRank.F, new DeferredHolder[]{}, new DeferredHolder[]{}));


    public static final DeferredItem<TestDummyItem> TEST_DUMMY = ITEMS.register("test_dummy",
            () -> new TestDummyItem((new Item.Properties())));

    public static final DeferredItem<DungeonItem> GOBLIN_MEAT = ITEMS.register("goblin_meat",
            () -> new DungeonItem((new Item.Properties().food(new FoodProperties(2,1,false, 2, Optional.empty(), List.of())))));

    public static final DeferredItem<DungeonItem>INFUSED_BREAD = ITEMS.register("infused_bread",
            () -> new DungeonItem((new Item.Properties().food(new FoodProperties(5,6,false, 1.6f, Optional.empty(), List.of())))));

    public static final DeferredItem<DungeonItem>INFUSED_WHEAT = ITEMS.register("infused_wheat",
            () -> new DungeonItem((new Item.Properties())));

    public static final DeferredItem<EffigyCurio> HEAVY_EFFIGY = ITEMS.register("heavy_effigy",
            () -> new EffigyCurio(new Item.Properties(),DungeonItemRank.F, ModClasses.TANK, null));

    public static final SimpleItemGroup.IngotAndNuggetAndRaw GILDREAN = new SimpleItemGroup.IngotAndNuggetAndRaw("gildrean");
    public static final SimpleItemGroup.IngotAndRaw INFERNAL_TIN = new SimpleItemGroup.IngotAndRaw("infernal_tin");
    public static final SimpleItemGroup.IngotAndRaw ARCTIC_IRON = new SimpleItemGroup.IngotAndRaw("arctic_iron");
    public static final SimpleItemGroup.IngotAndRaw LAVINTINE = new SimpleItemGroup.IngotAndRaw("lavintine");

    public static final DeferredItem<DungeonItem> GILDREAN_APPLE = ITEMS.register("gildrean_apple",
            () -> new DungeonItem((new Item.Properties()).rarity(Rarity.RARE).food(ModFoods.GIDLDREAN_APPLE)));

    public static final SimpleItemGroup.ArmorSet<InfusedAlloyArmorItem> INFUSED_ARMOR_SET = new SimpleItemGroup.ArmorSet<>(
            "infused_alloy",
            30,
            InfusedAlloyArmorItem::new,
            new Item.Properties()
    );

    public static final SimpleItemGroup.ArmorSet<DungeonScholarArmorItem> SCHOLAR_ARMOR_SET = new SimpleItemGroup.ArmorSet<>(
            "scholar",
            10,
            DungeonScholarArmorItem::new,
            new Item.Properties()
            );

    public static final SimpleItemGroup.ArmorSet<KobaltArmorItem> KOBALT_ARMOR_SET = new SimpleItemGroup.ArmorSet<>(
            "kobalt",
            40,
            KobaltArmorItem::new,
            new Item.Properties()
    );

    public static final SimpleItemGroup.ArmorSet<ArcticIroncladArmorItem> ARCTIC_IRONCLAD_ARMOR_SET = new SimpleItemGroup.ArmorSet<>(
            "arctic_ironclad",
            28,
            ArcticIroncladArmorItem::new,
            new Item.Properties()
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        ModSpawnEggs.register(eventBus);
    }
}
