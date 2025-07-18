package net.emsee.thedungeon.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.PalettedContainer;

public final class BiomeUtils {
    public static void setBiome(ServerLevel level ,BlockPos position, ResourceKey<Biome> biome) {
        LevelChunk chunk = level.getChunk(position.getX() >> 4, position.getZ() >> 4);
        var biomes = (PalettedContainer<Holder<Biome>>) chunk.getSection(chunk.getSectionIndex(position.getY())).getBiomes();
        biomes.getAndSetUnchecked(
                position.getX() & 3, position.getY() & 3, position.getZ() & 3,
                level.registryAccess().lookupOrThrow(Registries.BIOME)
                        .getOrThrow(biome)
        );
        chunk.setUnsaved(true);
    }
}
