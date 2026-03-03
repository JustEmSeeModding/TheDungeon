package net.emsee.thedungeon.block.custom;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.custom.dungeonBlockCopies.DungeonAmethystBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingRoselithBlock extends DungeonAmethystBlock {
    public static final MapCodec<BuddingRoselithBlock> CODEC = simpleCodec(BuddingRoselithBlock::new);
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    public MapCodec<BuddingRoselithBlock> codec() {
        return CODEC;
    }

    public BuddingRoselithBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(GROWTH_CHANCE) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate)) {
                block = ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get();
            } else if (blockstate.is(ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get();
            } else if (blockstate.is(ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get();
            } else if (blockstate.is(ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get();
            }

            if (block != null) {
                BlockState newState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                level.setBlockAndUpdate(blockpos, newState);
            }
        }

    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }
}
