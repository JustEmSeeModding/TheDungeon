package net.emsee.thedungeon.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public final class BlockUtils {
    public static Boolean NeverValidSpawn(BlockState state, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> type) {return false;}

    public static void forEachInArea(int X, int Y, int Z, int X2, int Y2, int Z2, ForEachMethod method, Level level, Consumer<BlockPos> consumer) {
        forEachInArea(X,Y,Z,X2,Y2,Z2,method,level, false,consumer);
    }

    public static void forEachInArea(BlockPos p1, BlockPos p2, ForEachMethod method, Level level, Consumer<BlockPos> consumer) {
        forEachInArea(p1.getX(),p1.getY(),p1.getZ(),p2.getX(),p2.getY(),p2.getZ(), method, level,consumer);
    }

    public static void forEachInArea(int X, int Y, int Z, int X2, int Y2, int Z2, ForEachMethod method, Level level, boolean forceOutsideWorld, Consumer<BlockPos> consumer) {
        if (!forceOutsideWorld) {
            if (Y > level.getMaxBuildHeight() && Y2 > level.getMaxBuildHeight()
                || (Y < level.getMinBuildHeight() && Y2 < level.getMinBuildHeight())) return;
            Y = Math.clamp(Y, level.getMinBuildHeight(), level.getMaxBuildHeight());
            Y2 = Math.clamp(Y2, level.getMinBuildHeight(), level.getMaxBuildHeight());
        }
        ForEach(X,Y,Z,X2,Y2,Z2,method,consumer);
    }

    public static void forEachInArea(BlockPos p1, BlockPos p2, ForEachMethod method, Level level, boolean forceOutsideWorld, Consumer<BlockPos> consumer) {
        forEachInArea(p1.getX(),p1.getY(),p1.getZ(),p2.getX(),p2.getY(),p2.getZ(), method,level, forceOutsideWorld,consumer);
    }

    private static void ForEach(int X, int Y, int Z, int X2, int Y2, int Z2, ForEachMethod method, Consumer<BlockPos> consumer){
        int minX = Math.min(X,X2);
        int maxX = Math.max(X,X2);
        int minY = Math.min(Y,Y2);
        int maxY = Math.max(Y,Y2);
        int minZ = Math.min(Z,Z2);
        int maxZ = Math.max(Z,Z2);

        BlockPos min = new BlockPos(minX, minY, minZ);
        BlockPos max = new BlockPos(maxX, maxY, maxZ);

        switch (method) {
            case BOTTOM_TO_TOP -> {
                for (int x = min.getX(); x <= max.getX(); x++)
                    for (int z = min.getZ(); z <= max.getZ(); z++)
                        for (int y = min.getY(); y <= max.getY(); y++) {
                            BlockPos pos = new BlockPos(x, y, z);
                            consumer.accept(pos);
                        }
            }
            case TOP_TO_BOTTOM -> {
                for (int x = min.getX(); x <= max.getX(); x++)
                    for (int z = min.getZ(); z <= max.getZ(); z++)
                        for (int y = max.getY(); y >= min.getY(); y--) {
                            BlockPos pos = new BlockPos(x, y, z);
                            consumer.accept(pos);
                        }
            }
        }
    }

    public enum ForEachMethod {
        BOTTOM_TO_TOP,
        TOP_TO_BOTTOM
    }
}
