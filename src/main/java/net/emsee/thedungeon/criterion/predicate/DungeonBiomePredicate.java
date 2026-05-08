package net.emsee.thedungeon.criterion.predicate;

import com.mojang.serialization.Codec;
import net.emsee.thedungeon.dungeon.registry.DungeonBiome;

public record DungeonBiomePredicate(DungeonBiome biome) {
    public static final Codec<DungeonBiomePredicate> CODEC = DungeonBiome.CODEC.xmap(DungeonBiomePredicate::new, DungeonBiomePredicate::biome);

    public static DungeonBiomePredicate of(DungeonBiome biome) {
        return new DungeonBiomePredicate(biome);
    }

    public boolean matches(DungeonBiome biome) {
        return this.biome == biome;
    }
}
