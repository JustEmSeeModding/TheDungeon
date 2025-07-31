package net.emsee.thedungeon.mobEffect;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, TheDungeon.MOD_ID);

    public static final Holder<MobEffect> HOB_GOBLIN_TRADEABLE = register("hob_goblin_tradeable", new ModMobEffect(MobEffectCategory.BENEFICIAL, 3402751));


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return MOB_EFFECTS.register(name, ()-> effect);
    }
}
