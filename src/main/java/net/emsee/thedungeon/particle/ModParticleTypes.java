package net.emsee.thedungeon.particle;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class ModParticleTypes {
    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<SimpleParticleType> LUMINOUS_BEAM =
            PARTICLE_TYPES.register("luminous_beam", () -> new SimpleParticleType(true));


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
