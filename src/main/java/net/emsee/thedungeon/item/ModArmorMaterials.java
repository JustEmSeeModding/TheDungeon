package net.emsee.thedungeon.item;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, TheDungeon.MOD_ID);

    public static final Holder<ArmorMaterial> INFUSED_ALLOY =
            ARMOR_MATERIALS.register("infused_alloy", () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 4);
                    map.put(ArmorItem.Type.LEGGINGS, 7);
                    map.put(ArmorItem.Type.CHESTPLATE, 9);
                    map.put(ArmorItem.Type.HELMET, 4);
                }), 0, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(ModItems.INFUSED_ALLOY_INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "infused_alloy"))),
                    1, .25f));

    public static final Holder<ArmorMaterial> DUNGEON_SCHOLAR =
            ARMOR_MATERIALS.register("dungeon_scholar", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 1);
                        map.put(ArmorItem.Type.LEGGINGS, 2);
                        map.put(ArmorItem.Type.CHESTPLATE, 3);
                        map.put(ArmorItem.Type.HELMET, 1);
                    }), 0, SoundEvents.ARMOR_EQUIP_LEATHER, Ingredient::of,
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "scholar"))),
                    0, 0));

    public static final Holder<ArmorMaterial> KOBALT =
            ARMOR_MATERIALS.register("kobalt", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 2);
                        map.put(ArmorItem.Type.LEGGINGS, 5);
                        map.put(ArmorItem.Type.CHESTPLATE, 6);
                        map.put(ArmorItem.Type.HELMET, 2);
                    }), 0, SoundEvents.ARMOR_EQUIP_IRON, ()->Ingredient.of(ModItems.KOBALT.INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "kobalt"))),
                    0, 0));

    public static final Holder<ArmorMaterial> ARCTIC_IRONCLAD =
            ARMOR_MATERIALS.register("arctic_ironclad", () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.HELMET, 3);
                    }), 0, SoundEvents.ARMOR_EQUIP_NETHERITE, ()->Ingredient.of(ModItems.ARCTIC_IRON.INGOT),
                    List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, "arctic_ironclad"))),
                    0, .25f));


    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
