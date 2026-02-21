package net.emsee.thedungeon.events;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.criterion.ModCriteriaTriggerTypes;
import net.emsee.thedungeon.datagen.ModCuriosDataProvider;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.emsee.thedungeon.dungeon.src.GlobalDungeonManager;
import net.emsee.thedungeon.item.custom.DungeonCurio;
import net.emsee.thedungeon.item.custom.EffigyCurio;
import net.emsee.thedungeon.item.custom.SoulBoundTotem;
import net.emsee.thedungeon.mobEffect.ModMobEffects;
import net.emsee.thedungeon.utils.ModDungeonTeleportHandling;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioCanEquipEvent;
import top.theillusivec4.curios.api.event.CurioCanUnequipEvent;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModEntityEvents {
    @SubscribeEvent
    public static void onPlayerDeath(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            DebugLog.logInfo(DebugLog.DebugType.WARNINGS, "ON_DEATH_CLONE");
            Player original = event.getOriginal();
            Player clone = event.getEntity();
            // Reset gamemode on death
            if (original.level().dimension().equals(ModDimensions.DUNGEON_LEVEL_KEY)) {
                ModDungeonTeleportHandling.setPlayerGameMode(clone, true);
            }

            if (SoulBoundTotem.hasOrHadInInventory(original)) {
                SoulBoundTotem.copyPlayerInventory(original, clone);
            } else
                EffigyCurio.keepEffigyInInventory(original, clone);
        }
    }

    @SubscribeEvent
    public static void onCurioDrops(CurioDropsEvent event) {
        DebugLog.logInfo(DebugLog.DebugType.WARNINGS, "curio Drops");

        if (!(event.getEntity() instanceof Player player)) return;

        curioEffigyDrops(event, player);
        curioSoulTotemDrops(event, player);
    }

    private static void curioSoulTotemDrops(CurioDropsEvent event, Player player) {
        AtomicBoolean hasTotem = new AtomicBoolean(false);

        event.getDrops().forEach(drop -> {
            if (hasTotem.get()) return;
            if (drop.getItem().getItem() instanceof SoulBoundTotem) {
                hasTotem.set(true);
            }
        });

        if (hasTotem.get()) {
            SoulBoundTotem.addPlayerData(player);
        }

        if (SoulBoundTotem.hasOrHadInInventory(player)) {
            event.getDrops().forEach(drop -> {
                player.addItem(drop.getItem());
            });
            event.getDrops().clear();
        }
    }

    private static void curioEffigyDrops(CurioDropsEvent event, Player player) {
        AtomicBoolean triggered = new AtomicBoolean(false);
        event.getDrops().removeIf(itemEntity -> {
            if (triggered.get()) {
                return false;
            }
            if (itemEntity.getItem() instanceof ItemStack stack && stack.getItem() instanceof EffigyCurio effigyCurio) {
                event.getCurioHandler().getCurios().get(ModCuriosDataProvider.EFFIGY_IDENTIFIER).getStacks().setStackInSlot(0, stack.copy());
                triggered.set(true);
                return true;
            }
            return false;
        });
    }

    @SubscribeEvent
    public static void onPlayerDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        DebugLog.logInfo(DebugLog.DebugType.WARNINGS, "player Drops");


        if (SoulBoundTotem.hasOrHadInInventory(player)) {
            event.getDrops().forEach(drop -> {
                player.addItem(drop.getItem());
            });
            event.getDrops().clear();
        }
    }

    @SubscribeEvent
    public static void playerBiomeTrigger(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (isInDungeon(player)) {
            // Biome Criterion
            if (player instanceof ServerPlayer serverPlayer) {
                DungeonBiome biome = GlobalDungeonManager.getBiomeForPlayer(player);
                if (biome != null) {
                    ModCriteriaTriggerTypes.ENTER_DUNGEON_BIOME.get().trigger(serverPlayer, biome);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCurioUnequip(CurioCanUnequipEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.isCreative()) {
            event.setUnequipResult(TriState.DEFAULT);
            return;
        }
        if (isInDungeon(player)) {
            event.setUnequipResult(TriState.FALSE);
            return;
        }
        if (Objects.equals(event.getSlotContext().identifier(), ModCuriosDataProvider.EFFIGY_IDENTIFIER)) {
            if (player.hasEffect(ModMobEffects.EFFIGY_LOCKED)) {
                event.setUnequipResult(TriState.FALSE);
            }
        }
    }

    @SubscribeEvent
    public static void onCurioUnequip(CurioCanEquipEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.isCreative()) {
            event.setEquipResult(TriState.DEFAULT);
            return;
        }
        /*if (isInDungeon(player)) {
            event.setEquipResult(TriState.FALSE);
            return;
        }*/
        if (Objects.equals(event.getSlotContext().identifier(), ModCuriosDataProvider.EFFIGY_IDENTIFIER)) {
            if (player.hasEffect(ModMobEffects.EFFIGY_LOCKED)) {
                event.setEquipResult(TriState.FALSE);
            }
        }

    }

    private static boolean isInDungeon(Player player) {
        return player.level().dimension().equals(ModDimensions.DUNGEON_LEVEL_KEY);
    }

}
