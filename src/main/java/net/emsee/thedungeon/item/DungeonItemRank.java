package net.emsee.thedungeon.item;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum DungeonItemRank {
    F,
    E,
    D,
    C,
    B,
    A,
    S,
    SS;


    public String getName() {
        return this.name();
    }

    public static DungeonItemRank getByName(String name) {
        return valueOf(name);
    }

    public MutableComponent getTranslatable() {
        return Component.translatable("thedungeon.itemrank."+getName().toLowerCase());
    }

    public static DungeonRank getNext(DungeonRank rank) {
        int i = 0;
        for (DungeonRank r : DungeonRank.values()) {
            if (r == rank) break;
            i++;
        }
        return DungeonRank.values()[(i + 1) % DungeonRank.values().length];
    }








    public static final PrimitiveCodec<DungeonRank> CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<DungeonRank> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(name -> {
                DungeonRank rank = DungeonRank.getByName(name);
                return rank != null ? DataResult.success(rank) : DataResult.error(() -> "Unknown DungeonRank: " + name);
            });
        }

        @Override
        public <T> T write(DynamicOps<T> ops, DungeonRank value) {
            return ops.createString(value.getName());
        }
    };
}
