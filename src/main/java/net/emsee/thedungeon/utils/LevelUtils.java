package net.emsee.thedungeon.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class LevelUtils {
    public static void ReplaceArea(Level level, BlockState block, int x, int y, int z, int xSize, int ySize, int zSize) {
        for (int iX = x; iX < x + xSize; iX++) {
            for (int iY = y; iY < y + ySize; iY++) {
                for (int iZ = z; iZ < z + zSize; iZ++) {
                    level.setBlock(new BlockPos(iX, iY, iZ), block, 0);
                }
            }
        }
    }
}
