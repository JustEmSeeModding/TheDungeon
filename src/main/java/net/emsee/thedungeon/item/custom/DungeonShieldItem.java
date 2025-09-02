package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.List;

public class DungeonShieldItem extends DungeonItem implements IDungeonToolTips, Equipable {
    protected static ResourceLocation BASE_ARMOR_ID = TheDungeon.defaultResourceLocation("shield_item_armor");
    protected static ResourceLocation BASE_ARMOR_TOUGHNESS_ID = TheDungeon.defaultResourceLocation("shielditem_armor_toughness");

    public DungeonShieldItem(Properties properties, float armor, float armorToughness) {
        super(properties.stacksTo(1).attributes(createAttributes(armor, armorToughness)));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(DUNGEON_ITEM_HOVER_MESSAGE);
    }

    public static ItemAttributeModifiers createAttributes(float armor, float armorToughness) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR, new AttributeModifier(BASE_ARMOR_ID, armor, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.OFFHAND)
                .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(BASE_ARMOR_TOUGHNESS_ID, armorToughness, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.OFFHAND).build();
    }
}
