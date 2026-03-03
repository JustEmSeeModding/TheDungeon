package net.emsee.thedungeon.simpleRegisterGroup;

import com.mojang.datafixers.util.Function3;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.custom.DungeonArmorItem;
import net.emsee.thedungeon.item.custom.DungeonItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleItemGroup {
    public final String name;

    protected SimpleItemGroup(String name) {
        this.name = name;
    }

    public abstract List<DeferredItem<?>> getAll();
    public final List<Item> getAllAsItem() {
        List<Item> toReturn = new ArrayList<>();
        getAll().forEach(item -> toReturn.add(item.get()));
        return toReturn;
    }
    public final List<ResourceKey<Item>> getAllAsResourceKey() {
        List<ResourceKey<Item>> toReturn = new ArrayList<>();
        getAll().forEach(item -> toReturn.add(item.getKey()));
        return toReturn;
    }
    public static class IngotAndRaw extends SimpleItemGroup implements WithIngot, WithRaw{
        public final DeferredItem<DungeonItem> INGOT;
        public final DeferredItem<DungeonItem> RAW;

        public IngotAndRaw(String name) {
            super(name);
            INGOT = ModItems.ITEMS.register(name + "_ingot", () -> new DungeonItem(new Item.Properties()));
            RAW = ModItems.ITEMS.register("raw_" + name, () -> new DungeonItem(new Item.Properties()));
        }

        @Override
        public List<DeferredItem<?>> getAll() {
            return List.of(INGOT, RAW);
        }

        @Override
        public DeferredItem<?> getIngot() {
            return INGOT;
        }
        @Override
        public DeferredItem<?> getRaw() {
            return RAW;
        }
    }

    public static class IngotAndNuggetAndRaw extends SimpleItemGroup implements WithIngot, WithRaw, WithNugget {
        public final DeferredItem<DungeonItem> INGOT;

        public final DeferredItem<DungeonItem> NUGGET;
        public final DeferredItem<DungeonItem> RAW;

        public IngotAndNuggetAndRaw(String name) {
            super(name);
            INGOT = ModItems.ITEMS.register(name + "_ingot", () -> new DungeonItem(new Item.Properties()));
            RAW = ModItems.ITEMS.register("raw_" + name, () -> new DungeonItem(new Item.Properties()));
            NUGGET = ModItems.ITEMS.register(name+"_nugget", () -> new DungeonItem(new Item.Properties()));
        }

        @Override
        public List<DeferredItem<?>> getAll() {
            return List.of(INGOT, NUGGET, RAW);
        }

        @Override
        public DeferredItem<?> getIngot() {
            return INGOT;
        }

        @Override
        public DeferredItem<?> getNugget() {
            return NUGGET;
        }

        @Override
        public DeferredItem<?> getRaw() {
            return RAW;
        }
    }

    public static class IngotAndNugget extends SimpleItemGroup implements WithIngot, WithNugget {
        public final DeferredItem<DungeonItem> INGOT;

        public final DeferredItem<DungeonItem> NUGGET;

        public IngotAndNugget(String name) {
            super(name);
            INGOT = ModItems.ITEMS.register(name + "_ingot", () -> new DungeonItem(new Item.Properties()));
            NUGGET = ModItems.ITEMS.register(name+"_nugget", () -> new DungeonItem(new Item.Properties()));
        }

        @Override
        public List<DeferredItem<?>> getAll() {
            return List.of(INGOT, NUGGET);
        }

        @Override
        public DeferredItem<?> getIngot() {
            return INGOT;
        }

        @Override
        public DeferredItem<?> getNugget() {
            return NUGGET;
        }
    }

    public static class ArmorSet<A extends DungeonArmorItem> extends SimpleItemGroup {
        public final DeferredItem<A> HELMET;
        public final DeferredItem<A> CHESTPLATE;
        public final DeferredItem<A> LEGGINGS;
        public final DeferredItem<A> BOOTS;

        public ArmorSet(String name, Holder<ArmorMaterial> material, int durabilityFactor, Function3<Holder<ArmorMaterial>,ArmorItem.Type,Item.Properties, A> aNew, Item.Properties baseProperties) {
            super(name);
            HELMET= ModItems.ITEMS.register(name + "_helmet",
                    () -> aNew.apply(material, ArmorItem.Type.HELMET, baseProperties.durability(ArmorItem.Type.BOOTS.getDurability(durabilityFactor))));
            CHESTPLATE = ModItems.ITEMS.register(name + "_chestplate",
                    () ->aNew.apply(material, ArmorItem.Type.CHESTPLATE, baseProperties.durability(ArmorItem.Type.CHESTPLATE.getDurability(durabilityFactor))));
            LEGGINGS = ModItems.ITEMS.register(name + "_leggings",
                    () ->aNew.apply(material, ArmorItem.Type.LEGGINGS, baseProperties.durability(ArmorItem.Type.LEGGINGS.getDurability(durabilityFactor))));
            BOOTS = ModItems.ITEMS.register(name + "_boots",
                    () ->aNew.apply(material, ArmorItem.Type.BOOTS, baseProperties.durability(ArmorItem.Type.BOOTS.getDurability(durabilityFactor))));
        }

        @Override
        public List<DeferredItem<?>> getAll() {
            return List.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS);
        }
    }

    public interface WithIngot {
        public DeferredItem<?> getIngot();
    }
    public interface WithRaw {
        public DeferredItem<?> getRaw();
    }
    public interface WithNugget {
        public DeferredItem<?> getNugget();
    }
}
