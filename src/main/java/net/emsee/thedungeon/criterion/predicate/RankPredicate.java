package net.emsee.thedungeon.criterion.predicate;

import com.mojang.serialization.Codec;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;

public record RankPredicate(DungeonRank rank) {
    public static final Codec<RankPredicate> CODEC = DungeonRank.CODEC.xmap(RankPredicate::new, RankPredicate::rank);

    public static RankPredicate of(DungeonRank rank) {
        return new RankPredicate(rank);
    }

    public boolean matches(DungeonRank rank) {
        return this.rank == rank;
    }
}
