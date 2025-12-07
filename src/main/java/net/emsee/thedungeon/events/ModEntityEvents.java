package net.emsee.thedungeon.events;

import net.emsee.thedungeon.DebugLog;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.item.custom.DungeonWeaponItem;
import net.emsee.thedungeon.utils.ModDungeonTeleportHandling;
import net.emsee.thedungeon.worldgen.dimention.ModDimensions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModEntityEvents {
    @SubscribeEvent
    public static void resetGamemodeOnPlayerDeath(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getOriginal().level().dimension().equals(ModDimensions.DUNGEON_LEVEL_KEY))
            ModDungeonTeleportHandling.setPlayerGameMode(event.getEntity(), true);
    }
}
