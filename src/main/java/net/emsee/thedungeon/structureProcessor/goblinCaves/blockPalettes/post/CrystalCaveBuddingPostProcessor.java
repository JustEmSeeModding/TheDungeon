package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes.post;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.entity.custom.CrystalGolemEntity;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.structureProcessor.Predicates;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class CrystalCaveBuddingPostProcessor extends BlockPaletteReplacementProcessor implements PostProcessor {
    public static final CrystalCaveBuddingPostProcessor INSTANCE = new CrystalCaveBuddingPostProcessor();

    public static final MapCodec<CrystalCaveBuddingPostProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    public static final int AMETHYST_BASE_WEIGHT =
            CrystalGolemEntity.Variant.AMETHYST.getWeight();
    public static final int ROSELITH_BASE_WEIGHT =
            CrystalGolemEntity.Variant.ROSELITH.getWeight();
    public static final int GARNETORE_BASE_WEIGHT =
            CrystalGolemEntity.Variant.GARNETORE.getWeight();
    public static final int VERDANTITE_BASE_WEIGHT =
            CrystalGolemEntity.Variant.VERDANTITE.getWeight();
    public static final int LUMANITE_BASE_WEIGHT =
            CrystalGolemEntity.Variant.LUMANITE.getWeight();

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.AIR,
                    Util.make(new WeightedMap.Int<>(), (clusterMap) -> {
                        for (Direction direction : Direction.values()) {

                            // Amethyst clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 2);

                            // Roselith clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_ROSELITH_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_ROSELITH_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_ROSELITH_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_BLOCK)), 2);

                            // Garnetore clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_GARNETORE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_GARNETORE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_GARNETORE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_BLOCK)), 2);

                            // Verdantite clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_VERDATITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDATITE_BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_VERDATITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDATITE_BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_VERDATITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDATITE_BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDATITE_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDATITE_BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDATITE_BLOCK)), 2);

                            // Lumanite clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_LUMANITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_LUMANITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_LUMANITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_BLOCK)), 2);

                            // Amethyst clusters (on calcite)
                            clusterMap.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 4);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 3);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 2);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 1);

                            // Roselith clusters (on calcite)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_ROSELITH_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 4 * ROSELITH_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_ROSELITH_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 3 * ROSELITH_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_ROSELITH_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 2 * ROSELITH_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 1 * ROSELITH_BASE_WEIGHT);

                            // Garnetore clusters (on calcite)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_GARNETORE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 4 * GARNETORE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_GARNETORE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 3 * GARNETORE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_GARNETORE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 2 * GARNETORE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 1 * GARNETORE_BASE_WEIGHT);

                            // Verdantite clusters (on calcite)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_VERDATITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 4 * VERDANTITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_VERDATITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 3 * VERDANTITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_VERDATITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 2 * VERDANTITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDATITE_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 1 * VERDANTITE_BASE_WEIGHT);

                            // Lumanite clusters (on calcite)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.SMALL_LUMANITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 4 * LUMANITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.MEDIUM_LUMANITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 3 * LUMANITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LARGE_LUMANITE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 2 * LUMANITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 1 * LUMANITE_BASE_WEIGHT);

                            // air (on calcite)
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 10000);
                        }
                        clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 2), 1);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }

    @Override
    public boolean skipBlockForProcessing(LevelReader level, BlockPos pos, BlockState state) {
        return PostProcessor.super.skipBlockForProcessing(level, pos, state) ||
                hasNoReplacementFor(state.getBlock());
    }
}
