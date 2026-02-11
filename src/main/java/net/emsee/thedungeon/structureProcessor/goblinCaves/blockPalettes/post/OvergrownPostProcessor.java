package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes.post;

import com.google.common.collect.Maps;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.structureProcessor.Predicates;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class OvergrownPostProcessor extends BlockPaletteReplacementProcessor implements PostProcessor {
    public static final OvergrownPostProcessor INSTANCE = new OvergrownPostProcessor();


    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.AIR,
                    Util.make(new WeightedMap.Int<>(), (extrasMap) -> {
                        extrasMap.put(new ReplaceInstance(Blocks.HANGING_ROOTS::defaultBlockState).withPredicate(new Predicates.BaseSolidBlockPredicate(Direction.UP)), 2);
                        extrasMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 30);
                    }));
        });
    }

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
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
