package net.emsee.thedungeon.events;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.dungeon.GlobalDungeonManager;
import net.emsee.thedungeon.item.custom.DungeonArmorItem;
import net.emsee.thedungeon.item.custom.DungeonPickaxeItem;
import net.emsee.thedungeon.item.interfaces.IDungeonItemSwapHandling;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class ModEvents {
    @SubscribeEvent
    public static void entityEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot().isArmor()) {
            if (event.getFrom().getItem() instanceof DungeonArmorItem dungeonArmorItem)
                dungeonArmorItem.UnEquip(event.getEntity(), event.getFrom(), event.getSlot());
            if (event.getTo().getItem() instanceof DungeonArmorItem dungeonArmorItem)
                dungeonArmorItem.equip(event.getEntity(), event.getTo(), event.getSlot());
            else if (!event.getTo().isEmpty() && event.getEntity().level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
                equippedNonDungeonInDungeon(event);
            }
        } else if (event.getSlot() == EquipmentSlot.MAINHAND || event.getSlot() == EquipmentSlot.OFFHAND) {
            if (event.getEntity() instanceof Player player) {
                player.getArmorSlots().forEach(stack -> {
                    if (stack.getItem() instanceof DungeonArmorItem dungeonArmorItem)
                        dungeonArmorItem.swapHandItem(player, event.getFrom(), event.getTo(), event.getSlot(), stack, player.getEquipmentSlotForItem(stack));
                });
            }
            if (event.getFrom().getItem() instanceof IDungeonItemSwapHandling swapHandling) {
                swapHandling.swapOutOfHand(event.getEntity(), event.getFrom(), event.getTo(), event.getSlot());
            }
            if (event.getTo().getItem() instanceof IDungeonItemSwapHandling swapHandling) {
                swapHandling.swapIntoHand(event.getEntity(), event.getTo(), event.getFrom(), event.getSlot());
            }
        }
    }

    @SubscribeEvent
    public static void entityDamaged(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            player.getArmorSlots().forEach(stack -> {
                if (stack.getItem() instanceof DungeonArmorItem dungeonArmorItem)
                    dungeonArmorItem.EntityPreDamaged(event);
            });

            float reduction = (float) player.getAttribute(ModAttributes.INCOMING_DAMAGE_REDUCTION).getValue();
            if (reduction > 0) {
                event.setNewDamage(event.getNewDamage() - reduction);
            }
        }
    }

    @SubscribeEvent
    public static void DungeonServerTick(ServerTickEvent.Pre event) {
        GlobalDungeonManager.Tick(event);
    }

    private static void equippedNonDungeonInDungeon(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.isCreative()) return;
            player.displayClientMessage(Component.translatable("message.thedungeon.equipped_wrong_armor"), false);
            player.getInventory().placeItemBackInInventory(player.getItemBySlot(event.getSlot()).copyAndClear());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().getMainHandItem().getItem() instanceof DungeonPickaxeItem diggerItem && event.getPlayer().level().dimension() == ModDimensions.DUNGEON_LEVEL_KEY) {
            diggerItem.breakEvent(event);
        }
    }
}
