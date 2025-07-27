package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.CrystalCaveProcessor;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Predicate;

public class CrystalCaveBuddingProcessor extends BlockPalletReplacementProcessor implements PostProcessor {
    public static final CrystalCaveBuddingProcessor INSTANCE = new CrystalCaveBuddingProcessor();

    public static final MapCodec<CrystalCaveBuddingProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    protected final WeightedMap.Int<ReplaceInstance> clusterMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                for (Direction direction : Direction.values()) {
                    map.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)).withPredicate(new ClusterPredicate(direction, Blocks.AMETHYST_BLOCK)), 4);
                    map.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)).withPredicate(new ClusterPredicate(direction, Blocks.AMETHYST_BLOCK)), 3);
                    map.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)).withPredicate(new ClusterPredicate(direction, Blocks.AMETHYST_BLOCK)), 2);
                    map.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)).withPredicate(new ClusterPredicate(direction, Blocks.AMETHYST_BLOCK)), 1);
                }
                map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 3);

            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.BUDDING_AMETHYST, clusterMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }

    protected static class ClusterPredicate implements Predicate<PredicateInfo> {
        final Direction direction;
        final Block baseBlock;

        ClusterPredicate(Direction direction, Block baseBlock) {
            this.direction=direction;
            this.baseBlock = baseBlock;
        }

        @Override
        public boolean test(PredicateInfo predicateInfo) {
            Block worldBaseBlock = switch (direction){
                case DOWN -> predicateInfo.level.getBlockState(predicateInfo.pos.above()).getBlock();
                case UP -> predicateInfo.level.getBlockState(predicateInfo.pos.below()).getBlock();
                case NORTH -> predicateInfo.level.getBlockState(predicateInfo.pos.south()).getBlock();
                case SOUTH -> predicateInfo.level.getBlockState(predicateInfo.pos.north()).getBlock();
                case WEST -> predicateInfo.level.getBlockState(predicateInfo.pos.east()).getBlock();
                case EAST -> predicateInfo.level.getBlockState(predicateInfo.pos.west()).getBlock();
            };

            return worldBaseBlock == baseBlock;
        }
    }
}
