package net.emsee.thedungeon.criterion;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCriteriaTriggerTypes {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES =
            DeferredRegister.create(Registries.TRIGGER_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<FailedDungeonTravelTrigger> FAILED_DUNGEON_TRAVEL =
            TRIGGER_TYPES.register("dungeon_travel", FailedDungeonTravelTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGER_TYPES.register(eventBus);
    }
}
