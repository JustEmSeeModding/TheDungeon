package net.emsee.thedungeon.dungeon.src;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum DungeonRank implements StringRepresentable {
    F(new BlockPos(0, 150, 0)),
    E(new BlockPos(3000, 150, 3000)),
    D(new BlockPos(-3000, 150, 3000)),
    C(new BlockPos(3000, 150, -3000)),
    B(new BlockPos(-3000, 150, -3000)),
    A(new BlockPos(6000, 150, 6000)),
    S(new BlockPos(-3000, 150, 6000)),
    SS(new BlockPos(6000, 150, -6000));

    private final BlockPos centerPos;

    DungeonRank(BlockPos centerPos) {
        this.centerPos = centerPos;
    }

    public BlockPos getDefaultCenterPos() {
        return centerPos;
    }

    public String getName() {
        return name();
    }

    public static DungeonRank getByName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Component getTranslatable() {
        return Component.translatable("thedungeon.rank."+getName().toLowerCase());
    }

    public static DungeonRank getNext(DungeonRank rank) {
        int i = 0;
        for (DungeonRank r : DungeonRank.values()) {
            if (r == rank) break;
            i++;
        }
        return DungeonRank.values()[(i + 1) % DungeonRank.values().length];
    }

    public static DungeonRank getClosestTo(BlockPos pos) {
        double lowestDist = -1;
        DungeonRank toReturn = null;
        for (DungeonRank rank : DungeonRank.values()) {
            double dist = rank.getDefaultCenterPos().distSqr(pos);
            if (lowestDist == -1 || dist < lowestDist) {
                lowestDist = dist;
                toReturn = rank;
            }
        }
        return toReturn;
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

    @Override
    public String getSerializedName() {
        return getName().toLowerCase();
    }
}
