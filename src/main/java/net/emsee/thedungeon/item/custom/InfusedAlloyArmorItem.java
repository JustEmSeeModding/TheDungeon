package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

public class InfusedAlloyArmorItem extends DungeonArmorItem {
    private final double speedPenalty = -.0075d;
    private final double aggroBoost = 25;
    private final double setHealthBonus = 4;


    public InfusedAlloyArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return super.getDefaultAttributeModifiers()
                .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.speed." + type.getName()), speedPenalty, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(ModAttributes.PLAYER_DUNGEON_AGGRO_TO_ENEMY, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.aggro." + type.getName()), aggroBoost, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(Attributes.MAX_HEALTH, new AttributeModifier(TheDungeon.defaultResourceLocation("infused_alloy.max_health_boost"), setHealthBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(EquipmentSlot.BODY));
    }
}
