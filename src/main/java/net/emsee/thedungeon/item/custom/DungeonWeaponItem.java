package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.emsee.thedungeon.item.interfaces.IDungeonWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.LinkedHashMap;
import java.util.List;

public class DungeonWeaponItem extends SwordItem implements IDungeonCarryItem, IDungeonToolTips, IDungeonWeapon {
    private static final float TWO_HAND_OCCUPIED_DAMAGE_REDUCTION_MULTIPLIER = -.30f;
    private static final float TWO_HAND_OCCUPIED_SPEED_REDUCTION_MULTIPLIER = -.35f;


    public static final ResourceLocation WEAPON_TYPE_CHANGE_ATTACK_DAMAGE_ID = ResourceLocation.withDefaultNamespace("weapon_type_attack_damage");
    public static final ResourceLocation  WEAPON_TYPE_CHANGE_ATTACK_SPEED_ID = ResourceLocation.withDefaultNamespace("weapon_type_attack_speed");

    private final WeaponType weaponType;
    private final boolean isSweeping;

    public DungeonWeaponItem(WeaponType weaponType, boolean isSweeping, Tier tier, Properties properties) {
        super(tier, properties.rarity(Rarity.RARE), createToolProperties());
        this.weaponType = weaponType;
        this.isSweeping = isSweeping;
    }

    public DungeonWeaponItem(WeaponType weaponType, boolean isSweeping, Tier tier, Item.Properties properties, Tool toolComponentData) {
        super(tier, properties.rarity(Rarity.RARE), toolComponentData);
        this.weaponType = weaponType;
        this.isSweeping = isSweeping;
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F), Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DungeonItem.DUNGEON_ITEM_HOVER_MESSAGE);
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }


    public static ItemAttributeModifiers createAttributes(Tier tier, int attackDamage, float attackSpeed) {
        return createAttributes(tier, (float) attackDamage, attackSpeed);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (attackDamage + tier.getAttackDamageBonus()), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static ItemAttributeModifiers createAttributes(float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public final void handsChanged(ItemStack thisStack, ItemStack otherStack
            , EquipmentSlot thisSlot, EquipmentSlot otherSlot) {
        DataComponentMap components = thisStack.getComponents();
        ItemAttributeModifiers attributeModifiers = components.get(DataComponents.ATTRIBUTE_MODIFIERS);
        assert attributeModifiers != null;
        ItemAttributeModifiers.Builder attributeBuilder = ItemAttributeModifiers.builder();

        final Double[] baseDamageSpeed = new Double[2];

        attributeModifiers.forEach(EquipmentSlotGroup.MAINHAND, (holder, modifier) -> {
            if (modifier.is(WEAPON_TYPE_CHANGE_ATTACK_DAMAGE_ID) || modifier.is(WEAPON_TYPE_CHANGE_ATTACK_SPEED_ID)) return;
            if (modifier.is(BASE_ATTACK_DAMAGE_ID))
                baseDamageSpeed[0] = modifier.amount();
            if (modifier.is(BASE_ATTACK_SPEED_ID))
                baseDamageSpeed[1] = modifier.amount();
            attributeBuilder.add(holder,modifier,EquipmentSlotGroup.MAINHAND);
        });

        switch (weaponType) {
            case SINGLE_HANDED -> handsChangedSingleHanded(thisStack, otherStack, thisSlot, otherSlot, attributeBuilder, baseDamageSpeed[0], baseDamageSpeed[1]);
            case TWO_HANDED -> handsChangedTwoHanded(thisStack, otherStack, thisSlot, otherSlot, attributeBuilder, baseDamageSpeed[0], baseDamageSpeed[1]);
            //case DUAL_WIELD -> handsChangedDualWield(thisStack,otherStack,thisSlot,otherSlot, attributeBuilder, baseDamageSpeed[0], baseDamageSpeed[1]);
        }

        DataComponentPatch patch = DataComponentPatch.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, attributeBuilder.build()).build();
        thisStack.applyComponentsAndValidate(patch);
    }

    protected void handsChangedSingleHanded(ItemStack thisStack, ItemStack otherStack, EquipmentSlot thisSlot, EquipmentSlot otherSlot,
                                          ItemAttributeModifiers.Builder attributeBuilder,
                                          double baseDamage, double baseSpeed) {

    }
    protected void handsChangedTwoHanded(ItemStack thisStack, ItemStack otherStack, EquipmentSlot thisSlot, EquipmentSlot otherSlot,
                                       ItemAttributeModifiers.Builder attributeBuilder,
                                       double baseDamage, double baseSpeed) {

        attributeBuilder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(WEAPON_TYPE_CHANGE_ATTACK_DAMAGE_ID, TWO_HAND_OCCUPIED_DAMAGE_REDUCTION_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND);
        attributeBuilder.add(Attributes.ATTACK_SPEED, new AttributeModifier(WEAPON_TYPE_CHANGE_ATTACK_SPEED_ID, TWO_HAND_OCCUPIED_SPEED_REDUCTION_MULTIPLIER, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.MAINHAND);

    }

    private boolean allowSweep() {
        return isSweeping;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof LivingEntity living &&
                !(living.getMainHandItem() == stack || living.getOffhandItem() == stack)) {
            handsChangedReset(stack);
        }
    }

    public final void handsChangedReset(ItemStack thisStack) {
        DataComponentMap components = thisStack.getComponents();
        ItemAttributeModifiers attributeModifiers = components.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (attributeModifiers==null) return;
        final ItemAttributeModifiers.Builder attributeBuilder= ItemAttributeModifiers.builder();
        attributeModifiers.forEach(EquipmentSlotGroup.MAINHAND, (holder, modifier) -> {
            if (modifier.is(WEAPON_TYPE_CHANGE_ATTACK_DAMAGE_ID) || modifier.is(WEAPON_TYPE_CHANGE_ATTACK_SPEED_ID)) return;
            attributeBuilder.add(holder,modifier,EquipmentSlotGroup.MAINHAND);
        });

        DataComponentPatch patch = DataComponentPatch.builder().set(DataComponents.ATTRIBUTE_MODIFIERS, attributeBuilder.build()).build();
        thisStack.applyComponentsAndValidate(patch);
    }

    @Override
    public LinkedHashMap<Component, Component[]> getPrefixComponents(ItemStack stack) {
        return Util.make(new LinkedHashMap<>(), map -> {
            map.put(Component.translatable("item.thedungeon.tooltip.weapon_type"),
                    new Component[]{
                            weaponType.getTranslatable().copy().withStyle(ChatFormatting.GREEN)
                    });
        });
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        if (itemAbility == ItemAbilities.SWORD_SWEEP) return allowSweep();
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }

    public boolean allowOffhandAttack() {
        return true;
    }

    public enum WeaponType{
        SINGLE_HANDED("single_hand"),
        TWO_HANDED("two_hand"),
        ;

        final String resourceName;

        WeaponType(String resourceName) {
            this.resourceName = resourceName;
        }

        public String getResourceName() {
            return resourceName;
        }

        public Component getTranslatable() {
            return Component.translatable("item.thedungeon.tooltip.weapon_type."+resourceName);
        }
    }
}


