package net.emsee.thedungeon.item.interfaces;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IDungeonItemSwapHandling {
    default void swapIntoHand(LivingEntity entity, ItemStack stack, ItemStack oldStack, EquipmentSlot slot) {};
    default void swapOutOfHand(LivingEntity entity, ItemStack stack, ItemStack newStack, EquipmentSlot slot) {};
}
