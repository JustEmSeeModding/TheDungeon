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
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class DungeonScholarArmorItem extends DungeonArmorItem{
    private final double movementSpeedBonus = .005d;
    private final double setMovementSpeedBonus = .0075d;
    private final double bootsStepBonus = .5d;


    public DungeonScholarArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    /*@Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(fullSetBonusHeader);
        String[] bonusText = ((setMovementSpeedBonus*1000)+"").split("\\.");
        if (Objects.equals(bonusText[1], "0"))
            tooltipComponents.add(Component.translatable("item.thedungeon.scholar_armor.set_bonus",bonusText[0]+"%").withStyle(beneficialModifier));
        else tooltipComponents.add(Component.translatable("item.thedungeon.scholar_armor.set_bonus",(setMovementSpeedBonus*1000)+"%").withStyle(beneficialModifier));
    }*/

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers toReturn =
                super.getDefaultAttributeModifiers()
                        .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.resourceLocation("scholar.speed."+type.getName()), movementSpeedBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()))
                        .withModifierAdded(Attributes.MOVEMENT_SPEED, new AttributeModifier(TheDungeon.resourceLocation("scholar.speed.full_set"), setMovementSpeedBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(EquipmentSlot.BODY));
        if (type == Type.BOOTS) {
            toReturn = toReturn.withModifierAdded(Attributes.STEP_HEIGHT, new AttributeModifier(TheDungeon.resourceLocation("scholar.step_height."+type.getName()), bootsStepBonus, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.bySlot(type.getSlot()));
        }
        return toReturn;
    }
/*
    @Override
    protected void onFullSetEquipped(LivingEntity entity) {
        Objects.requireNonNull(entity.getAttribute(Attributes.MOVEMENT_SPEED)).addOrUpdateTransientModifier(new AttributeModifier(TheDungeon.resourceLocation("scholar.speed.full_set"), setMovementSpeedBonus, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    protected void onFullSetUnEquipped(LivingEntity entity) {
        Objects.requireNonNull(entity.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(TheDungeon.resourceLocation("scholar.speed.full_set"));
    }*/
}
