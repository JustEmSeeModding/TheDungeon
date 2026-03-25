package net.emsee.thedungeon.item.custom.armor;



import com.google.common.collect.Multimap;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeonClass.DungeonClass;
import net.emsee.thedungeon.dungeonClass.DungeonSubClass;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.item.ModItems;
import net.emsee.thedungeon.item.custom.armor.client.ArmorClientExtension;
import net.emsee.thedungeon.item.custom.armor.client.provider.IArmorModelProvider;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * basic armor item.
 */
public abstract class ModeledDungeonArmorItem extends DungeonArmorItem {
    public ModeledDungeonArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, DungeonItemRank rank, DeferredHolder<DungeonClass, ?>[] classes, DeferredHolder<DungeonSubClass<?>, ?>[] subClasses) {
        super(material, type, properties, rank, classes, subClasses);
    }

    public abstract IArmorModelProvider createModelProvider();
    public abstract ResourceLocation createTextureLocation();

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean inner) {
        return createTextureLocation();
    }

    //region display / model
    /**
     * creates a custom texture for your armor in
     * <br>{@code <nameSpace>:textures/models/armor/custom/<id>.png}
     */
    public static ResourceLocation makeCustomTextureLocation(String id) {
        return TheDungeon.defaultResourceLocation("textures/models/armor/" + id + ".png");
    }

    //endregion
    /**
     * @param registry the Register to add to
     * @param baseName the base name of the armor
     * @param creator a lambda function to create an instance of the armor, mostly a method reference to the constructor
     * @return a Map mapping the ArmorType to the RegObj for the slot
     */
    public static <T extends ModeledDungeonArmorItem> Map<Type, DeferredItem<T>> createRegistry(DeferredRegister.Items registry, String baseName, Function<Type, T> creator) {
        return Util.make(new EnumMap<>(Type.class), map -> {
            for (Type type : Type.values()) {
                if (type != Type.BODY) map.put(type, registry.register(baseName + "_" + type.getName(), () -> creator.apply(type)));
            }
        });
    }
}
