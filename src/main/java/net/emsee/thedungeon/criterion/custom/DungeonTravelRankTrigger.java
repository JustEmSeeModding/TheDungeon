package net.emsee.thedungeon.criterion.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.emsee.thedungeon.criterion.ModCriteriaTriggerTypes;
import net.emsee.thedungeon.criterion.predicate.RankPredicate;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class DungeonTravelRankTrigger extends SimpleCriterionTrigger<DungeonTravelRankTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, DungeonRank rank) {
        this.trigger(player, instance -> instance.matches(rank));
    }

    public static Criterion<TriggerInstance> has_traveled() {
        return ModCriteriaTriggerTypes.TRAVEL_TO_RANK.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
    }

    public static Criterion<TriggerInstance> has_traveled_to(DungeonRank rank) {
        return ModCriteriaTriggerTypes.TRAVEL_TO_RANK.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(new RankPredicate(rank))));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<RankPredicate> rank) implements SimpleInstance {
        public static final Codec<DungeonTravelRankTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create((codec) -> codec.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), RankPredicate.CODEC.optionalFieldOf("rank").forGetter(TriggerInstance::rank)).apply(codec, TriggerInstance::new));

        public boolean matches(DungeonRank rank) {
            return this.rank.map(rankPredicate -> rankPredicate.matches(rank)).orElse(true);
        }
    }
}