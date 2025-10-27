package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.structureProcessor.Predicates;
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

public class CrystalCaveBuddingPostProcessor extends BlockPalletReplacementProcessor implements PostProcessor {
    public static final CrystalCaveBuddingPostProcessor INSTANCE = new CrystalCaveBuddingPostProcessor();

    public static final MapCodec<CrystalCaveBuddingPostProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    protected final WeightedMap.Int<ReplaceInstance> clusterMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                for (Direction direction : Direction.values()) {
                    // on amethyst
                    map.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 4);
                    map.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 3);
                    map.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 2);
                    map.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 1);
                    map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState,0).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.AMETHYST_BLOCK)), 2);

                    //on calcite
                    map.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 4);
                    map.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 3);
                    map.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 2);
                    map.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction),1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 1);
                    map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState,1).withPredicate(new Predicates.BaseBlockPredicate(direction.getOpposite(), Blocks.CALCITE)), 200);
                }
                map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState,2), 1);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.AIR, clusterMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
