package net.emsee.thedungeon.mobEffect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModMobEffect extends MobEffect {
    protected ModMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    protected ModMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }
}
