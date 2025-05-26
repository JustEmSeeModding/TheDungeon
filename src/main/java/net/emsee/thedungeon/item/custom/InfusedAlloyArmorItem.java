package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class InfusedAlloyArmorItem extends DungeonArmorItem{
    private final double speedPenalty = -.0075d;
    private final double setHealthBonus = 4;


    public InfusedAlloyArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        return super.getDefaultAttributeModifiers()
                .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.resourceLocation("infused_alloy.speed."+type.getName()), speedPenalty, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                .withModifierAdded(Attributes.MAX_HEALTH, new AttributeModifier(TheDungeon.resourceLocation("infused_alloy.max_health_boost"), setHealthBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(EquipmentSlot.BODY));
    }
}
