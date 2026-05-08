package net.emsee.thedungeon.particle;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.particle.custom.LuminousBeamParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ModParticleEngine {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.LUMINOUS_BEAM.get(), LuminousBeamParticle.BeamProvider::new);
    }
}
