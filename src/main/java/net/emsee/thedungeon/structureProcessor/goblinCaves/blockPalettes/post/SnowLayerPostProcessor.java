package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes.post;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.custom.DungeonPortal;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Predicate;

public class SnowLayerPostProcessor extends BlockPaletteReplacementProcessor implements PostProcessor {
    public static final SnowLayerPostProcessor INSTANCE = new SnowLayerPostProcessor();

    public static final MapCodec<SnowLayerPostProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }


    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.AIR, Util.make(new WeightedMap.Int<>(), (layerMap) -> {
                SnowLayerPredicate snowLayerPredicate = new SnowLayerPredicate();

                layerMap.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 1)).withPredicate(snowLayerPredicate), 5);
                layerMap.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 2)).withPredicate(snowLayerPredicate), 4);
                layerMap.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 3)).withPredicate(snowLayerPredicate), 3);
                layerMap.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 4)).withPredicate(snowLayerPredicate), 2);
                layerMap.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 5)).withPredicate(snowLayerPredicate), 1);

                layerMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 8);
            }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }

    protected static class SnowLayerPredicate implements Predicate<PredicateInfo> {


        SnowLayerPredicate() {
        }

        @Override
        public boolean test(PredicateInfo predicateInfo) {
            //BlockState worldBaseBlock = predicateInfo.level.getBlockState(predicateInfo.pos.below());
            //return worldBaseBlock.isFaceSturdy(predicateInfo.level,predicateInfo.pos,Direction.DOWN);

            BlockState blockstate = predicateInfo.level().getBlockState(predicateInfo.pos().below());
            if (blockstate.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON) || blockstate.getBlock() instanceof DungeonPortal) {
                return false;
            } else {
                return blockstate.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON) || Block.isFaceFull(blockstate.getCollisionShape(predicateInfo.level(), predicateInfo.pos().below()), Direction.UP) || blockstate.is(Blocks.SNOW) && (Integer) blockstate.getValue(SnowLayerBlock.LAYERS) == 8;
            }
        }
    }

    @Override
    public boolean skipBlockForProcessing(LevelReader level, BlockPos pos, BlockState state) {
        return PostProcessor.super.skipBlockForProcessing(level, pos, state) ||
                hasNoReplacementFor(state.getBlock());
    }
}
