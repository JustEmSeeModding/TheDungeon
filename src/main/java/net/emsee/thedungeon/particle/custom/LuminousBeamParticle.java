package net.emsee.thedungeon.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class LuminousBeamParticle extends GlowParticle {
    static final RandomSource RANDOM = RandomSource.create();
    protected LuminousBeamParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        this.lifetime = 10;
    }

    @OnlyIn(Dist.CLIENT)
    public static class BeamProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public BeamProvider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            LuminousBeamParticle particle = new LuminousBeamParticle(level, x, y, z, (.5 - RANDOM.nextDouble())*.5+xSpeed, ySpeed, (.5 - RANDOM.nextDouble())*.5+zSpeed, this.sprite);
            if (level.random.nextBoolean()) {
                particle.setColor(0.95F, 0.8F, 0.52F);
            } else {
                particle.setColor(0.92F, 0.75F, 0.3F);
            }

            particle.yd *= 0.2F;
            if (xSpeed == 0.0 && zSpeed == 0.0) {
                particle.xd *= 0.1F;
                particle.zd *= 0.1F;
            }

            particle.setLifetime((int)(8.0 / (level.random.nextDouble() * 0.8 + 0.2)));
            return particle;
        }
    }
}
