package net.emsee.thedungeon.dungeon.registry;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

public enum DungeonBiome {
    none,
    GOBLIN_MAGMA_CAVE,
    GOBLIN_ICE_CAVE,
    GOBLIN_CRYSTAL_CAVES;

    public static final PrimitiveCodec<DungeonBiome> CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<DungeonBiome> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(name -> {
                try {
                    DungeonBiome rank = DungeonBiome.valueOf(name);
                    return DataResult.success(rank);
                } catch (Exception e){
                    return DataResult.error(() -> "DataResult Error in biome CODEC:"+e);
                }
            });
        }

        @Override
        public <T> T write(DynamicOps<T> ops, DungeonBiome value) {
            return ops.createString(value.name());
        }
    };
}
