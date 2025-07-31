package net.emsee.thedungeon.item.custom.armor;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.custom.DungeonArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

public class DungeonScholarArmorItem extends DungeonArmorItem {
    private final double movementSpeedBonus = .005d;
    private final double setMovementSpeedBonus = .0075d;
    private final double bootsStepBonus = .5d;


    public DungeonScholarArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers toReturn =
                super.getDefaultAttributeModifiers()
                        .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.defaultResourceLocation("scholar.speed."+type.getName()), movementSpeedBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                        .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.defaultResourceLocation("scholar.speed.full_set"), setMovementSpeedBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(EquipmentSlot.BODY));
        if (type == Type.BOOTS) {
            toReturn = toReturn.withModifierAdded(Attributes.STEP_HEIGHT, new AttributeModifier(TheDungeon.defaultResourceLocation("scholar.step_height."+type.getName()), bootsStepBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()));
        }
        return toReturn;
    }
}
