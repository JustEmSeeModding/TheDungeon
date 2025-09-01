package net.emsee.thedungeon.events;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.custom.DungeonArmorItem;
import net.emsee.thedungeon.item.custom.DungeonToolItem;
import net.emsee.thedungeon.item.custom.DungeonWeaponItem;
import net.emsee.thedungeon.item.interfaces.IDungeonItemSwapHandling;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public final class ModEquipmentEvents {
    @SubscribeEvent
    public static void entityEquipmentChanged(LivingEquipmentChangeEvent event) {
        armorSwapChanges(event);
        handleSwapChanges(event);
        handleWeaponChanges(event);
    }

    private static void armorSwapChanges(LivingEquipmentChangeEvent event) {
        if (!event.getSlot().isArmor()) return;
        if (event.getFrom().getItem() instanceof DungeonArmorItem dungeonArmorItem)
            dungeonArmorItem.UnEquip(event.getEntity(), event.getFrom(), event.getSlot());
        if (event.getTo().getItem() instanceof DungeonArmorItem dungeonArmorItem)
            dungeonArmorItem.equip(event.getEntity(), event.getTo(), event.getSlot());
        else if (!event.getTo().isEmpty() && event.getEntity().level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            equippedNonDungeonInDungeon(event);
        }
    }

    private static void equippedNonDungeonInDungeon(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isCreative()) return;
            player.displayClientMessage(Component.translatable("message.thedungeon.equipped_wrong_armor"), false);
            player.getInventory().placeItemBackInInventory(player.getItemBySlot(event.getSlot()).copyAndClear());
        }
    }

    private static void handleSwapChanges(LivingEquipmentChangeEvent event) {
        if (!(event.getSlot() == EquipmentSlot.MAINHAND || event.getSlot() == EquipmentSlot.OFFHAND)) return;
        // handle hand change for old item
        if (event.getFrom().getItem() instanceof IDungeonItemSwapHandling swapHandling) {
            swapHandling.swapOutOfHand(event.getEntity(), event.getFrom(), event.getTo(), event.getSlot());
        }
        // handle hand change for new item
        if (event.getTo().getItem() instanceof IDungeonItemSwapHandling swapHandling) {
            swapHandling.swapIntoHand(event.getEntity(), event.getTo(), event.getFrom(), event.getSlot());
        }

        // handle hand changes for armor
        if (event.getEntity() instanceof Player player) {
            player.getArmorSlots().forEach(stack -> {
                if (stack.getItem() instanceof DungeonArmorItem dungeonArmorItem)
                    dungeonArmorItem.swapHandItem(player, event.getFrom(), event.getTo(), event.getSlot(), stack, player.getEquipmentSlotForItem(stack));
            });
        }
    }

    private static void test(ItemAttributeModifierEvent event) {
    }

    private static void handleWeaponChanges(LivingEquipmentChangeEvent event) {
        if (!(event.getSlot() == EquipmentSlot.MAINHAND || event.getSlot() == EquipmentSlot.OFFHAND))
            return;
        EquipmentSlot otherSlot = event.getSlot() == EquipmentSlot.MAINHAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        ItemStack otherStack = event.getEntity().getItemBySlot(otherSlot);
        // if the new stack is a weapon, handle attribute changes
        if (event.getTo().getItem() instanceof DungeonWeaponItem weaponItem) {
            weaponItem.handsChanged(event.getTo(), otherStack, event.getSlot(), otherSlot);
        }
        // if the other hand slot is a weapon, handle attribute changes
        if (otherStack.getItem() instanceof DungeonWeaponItem otherWeaponItem) {
            otherWeaponItem.handsChanged(otherStack, event.getTo(), otherSlot, event.getSlot());
        }
    }

    @SubscribeEvent
    public static void armorOnEntityDamaged(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            player.getArmorSlots().forEach(stack -> {
                if (stack.getItem() instanceof DungeonArmorItem dungeonArmorItem)
                    dungeonArmorItem.EntityPreDamaged(event);
            });
        }
    }

    @SubscribeEvent
    public static void onDiggerItemBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().getMainHandItem().getItem() instanceof DungeonToolItem diggerItem && event.getPlayer().level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            diggerItem.breakEvent(event);
        }
    }
}
