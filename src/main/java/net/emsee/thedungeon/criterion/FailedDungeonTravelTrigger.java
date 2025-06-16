package net.emsee.thedungeon.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class FailedDungeonTravelTrigger extends SimpleCriterionTrigger<FailedDungeonTravelTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::matches);
    }

    public static Criterion<TriggerInstance> criterion() {
        return ModCriteriaTriggerTypes.FAILED_DUNGEON_TRAVEL.get().createCriterion(new TriggerInstance(Optional.empty()));

    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<FailedDungeonTravelTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create((codec) -> codec.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player)).apply(codec, TriggerInstance::new));

        public boolean matches() {
            return true;
        }
    }
}