package net.emsee.thedungeon.dungeon.util;

import net.minecraft.core.BlockPos;

public enum DungeonRank {
    F(new BlockPos(0, 150, 0), "F"),
    E(new BlockPos(15000, 150, 15000), "E"),
    D(new BlockPos(-15000, 150, 15000), "D"),
    C(new BlockPos(15000, 150, -15000), "C"),
    B(new BlockPos(-15000, 150, -15000), "B"),
    A(new BlockPos(30000, 150, 30000), "A"),
    S(new BlockPos(-30000, 150, 30000), "S"),
    SS(new BlockPos(30000, 150, -30000), "SS");

    private final BlockPos centerPos;
    private final String name;

    DungeonRank(BlockPos centerPos, String name) {
        this.centerPos = centerPos;
        this.name = name;
    }

    public BlockPos getDefaultCenterPos() {
        return centerPos;
    }

    public String getName() {
        return name;
    }

    public static DungeonRank getByName(String name) {
        for (DungeonRank rank : DungeonRank.values())
            if (name.equals(rank.getName())) return rank;
        return null;
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
}
