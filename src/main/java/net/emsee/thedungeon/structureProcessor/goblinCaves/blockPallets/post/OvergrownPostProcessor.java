package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post;

import com.google.common.collect.Maps;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.structureProcessor.Predicates;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class OvergrownPostProcessor extends BlockPalletReplacementProcessor implements PostProcessor {
    public static final OvergrownPostProcessor INSTANCE = new OvergrownPostProcessor();



    protected final WeightedMap.Int<ReplaceInstance> airReplaceMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.HANGING_ROOTS::defaultBlockState).withPredicate(new Predicates.BaseSolidBlockPredicate(Direction.UP)), 2);

                map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 30);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.AIR, airReplaceMap);
                //map.put(Blocks.SNOW_BLOCK, snowBlockMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
