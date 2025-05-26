package net.emsee.thedungeon.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockUtils {
    public static Boolean NeverValidSpawn(BlockState state, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> type) {
        return false;
    }
}
