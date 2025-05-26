package net.emsee.thedungeon.damageType;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.*;

import java.util.HashMap;
import java.util.Map;

public final class ModDamageTypes {
    private static final Map<ResourceKey<DamageType>, DamageType> DAMAGE_TYPES = new HashMap<>();

    public static final ResourceKey<DamageType> UNSTABLE_PORTAL = register("unstable_portal", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, .1f, DamageEffects.HURT, DeathMessageType.DEFAULT);
    public static final ResourceKey<DamageType> DUNGEON_RESET = register("dungeon_reset", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, .1f, DamageEffects.HURT, DeathMessageType.DEFAULT);

    public static void bootstrap(BootstrapContext<DamageType> context) {
        for (ResourceKey<DamageType> key : DAMAGE_TYPES.keySet()) {
            context.register(key, DAMAGE_TYPES.get(key));
        }
    }

    private static ResourceKey<DamageType> register(String path, DamageScaling damageScaling, float exhaustion, DamageEffects damageEffect, DeathMessageType deathMessageType) {
        ResourceKey<DamageType> toReturn = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TheDungeon.MOD_ID, path));
        DAMAGE_TYPES.put(toReturn, new DamageType(
                TheDungeon.MOD_ID + "." + path,
                damageScaling,
                exhaustion,
                damageEffect,
                deathMessageType));
        return toReturn;
    }
}
