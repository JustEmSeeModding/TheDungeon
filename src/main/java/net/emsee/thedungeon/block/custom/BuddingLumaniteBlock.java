package net.emsee.thedungeon.block.custom;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.block.custom.dungeonBlockCopies.DungeonAmethystBlock;
import net.emsee.thedungeon.block.custom.interfaces.IDungeonPickaxeMinable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class BuddingLumaniteBlock extends DungeonAmethystBlock {
    public static final MapCodec<BuddingLumaniteBlock> CODEC = simpleCodec(BuddingLumaniteBlock::new);
    public static final int GROWTH_CHANCE = 5;
    private static final Direction[] DIRECTIONS = Direction.values();

    public MapCodec<BuddingLumaniteBlock> codec() {
        return CODEC;
    }

    public BuddingLumaniteBlock(Properties properties) {
        super(properties);
    }

    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(GROWTH_CHANCE) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockpos = pos.relative(direction);
            BlockState blockstate = level.getBlockState(blockpos);
            Block block = null;
            if (canClusterGrowAtState(blockstate)) {
                block = ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get();
            } else if (blockstate.is(ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get();
            } else if (blockstate.is(ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get();
            } else if (blockstate.is(ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get()) && blockstate.getValue(AmethystClusterBlock.FACING) == direction) {
                block = ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get();
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
