package net.emsee.thedungeon.criterion.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.emsee.thedungeon.criterion.ModCriteriaTriggerTypes;
import net.emsee.thedungeon.criterion.predicate.DungeonBiomePredicate;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class DungeonBiomeTrigger extends SimpleCriterionTrigger<DungeonBiomeTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, DungeonBiome biome) {
        this.trigger(player, instance -> instance.matches(biome));
    }

    public static Criterion<TriggerInstance> has_traveled() {
        return ModCriteriaTriggerTypes.ENTER_DUNGEON_BIOME.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty()));
    }

    public static Criterion<TriggerInstance> has_traveled_to(DungeonBiome biome) {
        return ModCriteriaTriggerTypes.ENTER_DUNGEON_BIOME.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(new DungeonBiomePredicate(biome))));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<DungeonBiomePredicate> biome) implements SimpleInstance {
        public static final Codec<DungeonBiomeTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create((codec) -> codec.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), DungeonBiomePredicate.CODEC.optionalFieldOf("biome").forGetter(TriggerInstance::biome)).apply(codec, TriggerInstance::new));

        public boolean matches(DungeonBiome biome) {
            return this.biome.map(biomePredicate -> biomePredicate.matches(biome)).orElse(true);
        }
    }
}