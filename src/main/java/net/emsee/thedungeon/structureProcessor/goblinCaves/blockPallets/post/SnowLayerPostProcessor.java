package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets.post;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.custom.portal.DungeonPortal;
import net.emsee.thedungeon.block.custom.portal.DungeonPortalB;
import net.emsee.thedungeon.dungeon.src.types.Dungeon;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.structureProcessor.PostProcessor;
import net.emsee.thedungeon.utils.BlockUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Predicate;

public class SnowLayerPostProcessor extends BlockPalletReplacementProcessor implements PostProcessor {
    public static final SnowLayerPostProcessor INSTANCE = new SnowLayerPostProcessor();

    public static final MapCodec<SnowLayerPostProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public BlockUtils.ForEachMethod getMethod() {
        return BlockUtils.ForEachMethod.BOTTOM_TO_TOP;
    }

    protected final WeightedMap.Int<ReplaceInstance> smallLayerMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 1)).withPredicate(new SnowLayerPredicate()), 5);
                map.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 2)).withPredicate(new SnowLayerPredicate()), 4);
                map.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 3)).withPredicate(new SnowLayerPredicate()), 3);
                map.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 4)).withPredicate(new SnowLayerPredicate()), 2);
                map.put(new ReplaceInstance(() -> Blocks.SNOW.defaultBlockState().setValue(SnowLayerBlock.LAYERS, 5)).withPredicate(new SnowLayerPredicate()), 1);

                map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 8);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.AIR, smallLayerMap);
                //map.put(Blocks.SNOW_BLOCK, snowBlockMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

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

            BlockState blockstate = predicateInfo.level.getBlockState(predicateInfo.pos.below());
            if (blockstate.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON) || blockstate.getBlock() instanceof DungeonPortal) {
                return false;
            } else {
                return blockstate.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON) || Block.isFaceFull(blockstate.getCollisionShape(predicateInfo.level, predicateInfo.pos.below()), Direction.UP) || blockstate.is(Blocks.SNOW) && (Integer) blockstate.getValue(SnowLayerBlock.LAYERS) == 8;
            }
        }
    }
}
