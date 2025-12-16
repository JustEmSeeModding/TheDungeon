package net.emsee.thedungeon.item;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
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
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public MutableComponent getTranslatable() {
        return Component.translatable("thedungeon.itemrank."+getName().toLowerCase());
    }

    public static DungeonItemRank getNext(DungeonItemRank rank) {
        int i = 0;
        for (DungeonItemRank r : DungeonItemRank.values()) {
            if (r == rank) break;
            i++;
        }
        return DungeonItemRank.values()[(i + 1) % DungeonItemRank.values().length];
    }


    public static final PrimitiveCodec<DungeonItemRank> CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<DungeonItemRank> read(DynamicOps<T> ops, T input) {
            return ops.getStringValue(input).flatMap(name -> {
                DungeonItemRank rank = DungeonItemRank.getByName(name);
                return rank != null ? DataResult.success(rank) : DataResult.error(() -> "Unknown DungeonItemRank: " + name);
            });
        }

        @Override
        public <T> T write(DynamicOps<T> ops, DungeonItemRank value) {
            return ops.createString(value.getName());
        }
    };
}
