package net.emsee.thedungeon.item.custom.armor;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.dungeonClass.ModClasses;
import net.emsee.thedungeon.item.DungeonItemRank;
import net.emsee.thedungeon.item.ModArmorMaterials;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredHolder;

public class InfusedAlloyArmorItem extends DungeonArmorItem {
    private static final double speedPenalty = -.006d;
    private static final double aggroBoost = 25;
    private static final double setHealthBonus = 4;


    public InfusedAlloyArmorItem(Type type, Properties properties) {
        super(ModArmorMaterials.INFUSED_ALLOY, type, properties, DungeonItemRank.F, new DeferredHolder[]{ModClasses.TANK}, new DeferredHolder[]{});
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return super.getDefaultAttributeModifiers()
                .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.speed." + type.getName()), speedPenalty, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(ModAttributes.PLAYER_DUNGEON_AGGRO_TO_ENEMY, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.aggro." + type.getName()), aggroBoost, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(Attributes.MAX_HEALTH, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.max_health_boost"), setHealthBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(EquipmentSlot.BODY));
    }
}
