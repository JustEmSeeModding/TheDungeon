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
import java.util.function.Predicate;

public class CrystalCaveBuddingPostProcessor extends BlockPaletteReplacementProcessor implements PostProcessor {
    public static final CrystalCaveBuddingPostProcessor INSTANCE = new CrystalCaveBuddingPostProcessor();

    public static final MapCodec<CrystalCaveBuddingPostProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    public static final int AMETHYST_BASE_WEIGHT = 3;
    public static final int ROSELITH_BASE_WEIGHT = 5;
    public static final int GARNETORE_BASE_WEIGHT = 3;
    public static final int VERDANTITE_BASE_WEIGHT = 2;
    public static final int LUMANITE_BASE_WEIGHT = 4;

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
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_CRYSTAL_GROUP.BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_CRYSTAL_GROUP.BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_CRYSTAL_GROUP.BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_CRYSTAL_GROUP.BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.ROSELITH_CRYSTAL_GROUP.BLOCK)), 2);

                            // Garnetore clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_CRYSTAL_GROUP.BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_CRYSTAL_GROUP.BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_CRYSTAL_GROUP.BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_CRYSTAL_GROUP.BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.GARNETORE_CRYSTAL_GROUP.BLOCK)), 2);

                            // Verdantite clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDANTITE_CRYSTAL_GROUP.BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDANTITE_CRYSTAL_GROUP.BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDANTITE_CRYSTAL_GROUP.BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDANTITE_CRYSTAL_GROUP.BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.VERDANTITE_CRYSTAL_GROUP.BLOCK)), 2);

                            // Lumanite clusters (on base block)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_CRYSTAL_GROUP.BLOCK)), 4);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_CRYSTAL_GROUP.BLOCK)), 3);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_CRYSTAL_GROUP.BLOCK)), 2);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_CRYSTAL_GROUP.BLOCK)), 1);
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), ModBlocks.LUMANITE_CRYSTAL_GROUP.BLOCK)), 2);

                            Predicate<PredicateInfo> CalciteBasePredicate = new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE).or(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.GRANITE));
                            
                            // Amethyst clusters (on stone)
                            clusterMap.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 8 * AMETHYST_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 6 * AMETHYST_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 4 * AMETHYST_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 2 * AMETHYST_BASE_WEIGHT);

                            // Roselith clusters (on stone)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 8 * ROSELITH_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 6 * ROSELITH_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 4 * ROSELITH_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 2 * ROSELITH_BASE_WEIGHT);

                            // Garnetore clusters (on stone)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 8 * GARNETORE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 6 * GARNETORE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 4 * GARNETORE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 2 * GARNETORE_BASE_WEIGHT);

                            // Verdantite clusters (on stone)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 8 * VERDANTITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 6 * VERDANTITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 4 * VERDANTITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.VERDANTITE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 2 * VERDANTITE_BASE_WEIGHT);

                            // Lumanite clusters (on stone)
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.SMALL_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 8 * LUMANITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.MEDIUM_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 6 * LUMANITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.LARGE_BUD.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 4 * LUMANITE_BASE_WEIGHT);
                            clusterMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_CRYSTAL_GROUP.CLUSTERS.CLUSTER.get().defaultBlockState().setValue(AmethystClusterBlock.FACING, direction), 1).withPredicate(CalciteBasePredicate), 2 * LUMANITE_BASE_WEIGHT);

                            // air (on stone)
                            clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 1).withPredicate(CalciteBasePredicate), 1000);
                        }
                        //clusterMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, 2), 1);
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
