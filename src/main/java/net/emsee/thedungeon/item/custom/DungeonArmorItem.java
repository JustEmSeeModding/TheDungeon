package net.emsee.thedungeon.item.custom;

import net.emsee.thedungeon.item.interfaces.IDungeonCarryItem;
import net.emsee.thedungeon.item.interfaces.IDungeonToolTips;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class DungeonArmorItem extends ArmorItem implements IDungeonCarryItem, IDungeonToolTips {
    public DungeonArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties.rarity(Rarity.RARE));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(DUNGEON_ITEM_HOVER_MESSAGE);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof Player player) {
                if(!level.isClientSide && playerHasFullSetOfArmor(player) && playerHasFullArmorOn(player)) {
                    onFullSetTick(player);
                }
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    private boolean playerHasFullArmorOn(Player player) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem boots = (ArmorItem) player.getInventory().getArmor(0).getItem();
        ArmorItem leggings = (ArmorItem) player.getInventory().getArmor(1).getItem();
        ArmorItem chestplate = (ArmorItem) player.getInventory().getArmor(2).getItem();
        ArmorItem helmet = (ArmorItem) player.getInventory().getArmor(3).getItem();

        return boots.getMaterial() == helmet.getMaterial() && leggings.getMaterial() == helmet.getMaterial() && chestplate.getMaterial() == helmet.getMaterial();
    }

    private boolean playerHadFullArmorOn(Player player, ItemStack removedStack, EquipmentSlot slot) {
        if (!(removedStack.getItem() instanceof ArmorItem)) {
            return false;
        }

        Item boots = removedStack.getItem();
        Item leggings = removedStack.getItem();
        Item chestplate = removedStack.getItem();
        Item helmet = removedStack.getItem();

        if (slot != EquipmentSlot.FEET)  boots = player.getInventory().getArmor(0).getItem();
        if (slot != EquipmentSlot.LEGS)  leggings = player.getInventory().getArmor(1).getItem();
        if (slot != EquipmentSlot.CHEST)  chestplate = player.getInventory().getArmor(2).getItem();
        if (slot != EquipmentSlot.HEAD)  helmet = player.getInventory().getArmor(3).getItem();

        if (boots instanceof DungeonArmorItem armorBoots && helmet instanceof DungeonArmorItem armorHelmet && leggings instanceof DungeonArmorItem armorLeggings && chestplate instanceof DungeonArmorItem armorChestplate)
            return armorBoots.getMaterial() == armorHelmet.getMaterial() && armorLeggings.getMaterial() == armorHelmet.getMaterial() && armorChestplate.getMaterial() == armorHelmet.getMaterial();
        return false;
    }

    private boolean playerHasFullSetOfArmor(Player player) {
        return
                !player.getInventory().getArmor(0).isEmpty() &&
                !player.getInventory().getArmor(1).isEmpty() &&
                !player.getInventory().getArmor(2).isEmpty() &&
                !player.getInventory().getArmor(3).isEmpty();
    }

    public final void UnEquip(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        if (slot.isArmor() )
            onPieceUnEquip(entity, stack, slot);
        if (entity instanceof Player player) {
            if (playerHadFullArmorOn(player, stack, slot)) {
                onFullSetUnEquipped(player);
                removeFullSetAttributes(player, stack);
            }
        }
    }
    public final void equip(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {
        if (slot.isArmor())
            onPieceEquip(entity, stack, slot);
        if (entity instanceof Player player) {
            if (playerHasFullArmorOn(player)) {
                onFullSetEquipped(player);
                addFullSetAttributes(player, stack);
            }
        }
    }

    public final void swapHandItem(LivingEntity entity, ItemStack oldHandItem, ItemStack newHandItem, EquipmentSlot handSlot, ItemStack armorItem, EquipmentSlot armorSlot) {
        if (entity instanceof Player player) {
            if (playerHasFullArmorOn(player))
                onFullSetHandItemSwitched(entity, oldHandItem, newHandItem, handSlot, armorItem, armorSlot);
            onPieceHandItemSwitched(entity,oldHandItem, newHandItem,handSlot,armorItem,armorSlot);
        }
    }

    private void addFullSetAttributes(LivingEntity entity, ItemStack stack) {
        stack.getAttributeModifiers().modifiers().forEach(entry -> {
            if (entry.slot() == EquipmentSlotGroup.BODY) {
                entity.getAttribute(entry.attribute()).addOrUpdateTransientModifier(entry.modifier());
            }
        });
    }

    private void removeFullSetAttributes(LivingEntity entity, ItemStack stack) {
        stack.getAttributeModifiers().modifiers().forEach(entry -> {
            if (entry.slot() == EquipmentSlotGroup.BODY) {
                entity.getAttribute(entry.attribute()).removeModifier(entry.modifier());
            }
        });
    }

    public final void EntityPreDamaged(LivingDamageEvent.Pre event) {
        preWearerDamaged(event);
    }

    protected void preWearerDamaged(LivingDamageEvent.Pre event) {}
    protected void onFullSetTick(LivingEntity entity) {}
    protected void onPieceEquip(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {}
    protected void onPieceUnEquip(LivingEntity entity, ItemStack stack, EquipmentSlot slot) {}
    protected void onFullSetEquipped(LivingEntity entity) {}
    protected void onFullSetUnEquipped(LivingEntity entity) {}
    protected void onPieceHandItemSwitched(LivingEntity entity, ItemStack oldHandItem, ItemStack newHandItem, EquipmentSlot handSlot, ItemStack armorItem, EquipmentSlot armorSlot) {}
    protected void onFullSetHandItemSwitched(LivingEntity entity, ItemStack oldHandItem, ItemStack newHandItem, EquipmentSlot handSlot, ItemStack armorItem, EquipmentSlot armorSlot) {}
}
