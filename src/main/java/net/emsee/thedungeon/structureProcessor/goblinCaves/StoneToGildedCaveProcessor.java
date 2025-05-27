package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;

public class StoneToGildedCaveProcessor extends StructureProcessor {
    public static final StoneToGildedCaveProcessor INSTANCE = new StoneToGildedCaveProcessor();

    public static final MapCodec<StoneToGildedCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final Map<Block, Integer> defaultMap = Map.of(Blocks.BLACKSTONE,10, Blocks.GILDED_BLACKSTONE, 1);

    private final Map<Block, Map<Block, Integer>> replacements = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.COBBLESTONE, defaultMap);
        map.put(Blocks.MOSSY_COBBLESTONE, defaultMap);
        map.put(Blocks.STONE, defaultMap);
        map.put(Blocks.STONE_BRICKS, Map.of(Blocks.POLISHED_BLACKSTONE_BRICKS,1));
        map.put(Blocks.MOSSY_STONE_BRICKS, Map.of(Blocks.POLISHED_BLACKSTONE_BRICKS,1));
        map.put(Blocks.COBBLESTONE_STAIRS, Map.of(Blocks.BLACKSTONE_STAIRS,1));
        map.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Map.of(Blocks.BLACKSTONE_STAIRS,1));
        map.put(Blocks.STONE_STAIRS, Map.of(Blocks.BLACKSTONE_STAIRS,1));
        map.put(Blocks.STONE_BRICK_STAIRS, Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,1));
        map.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS,1));
        map.put(Blocks.COBBLESTONE_SLAB, Map.of(Blocks.BLACKSTONE_SLAB,1));
        map.put(Blocks.MOSSY_COBBLESTONE_SLAB, Map.of(Blocks.BLACKSTONE_SLAB,1));
        map.put(Blocks.SMOOTH_STONE_SLAB, Map.of(Blocks.POLISHED_BLACKSTONE_SLAB,1));
        map.put(Blocks.STONE_SLAB, Map.of(Blocks.BLACKSTONE_SLAB,1));
        map.put(Blocks.STONE_BRICK_SLAB, Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,1));
        map.put(Blocks.MOSSY_STONE_BRICK_SLAB, Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB,1));
        map.put(Blocks.STONE_BRICK_WALL, Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_WALL,1));
        map.put(Blocks.MOSSY_STONE_BRICK_WALL, Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_WALL,1));
        map.put(Blocks.COBBLESTONE_WALL, Map.of(Blocks.BLACKSTONE_WALL,1));
        map.put(Blocks.MOSSY_COBBLESTONE_WALL, Map.of(Blocks.BLACKSTONE_WALL,1));
        map.put(Blocks.CHISELED_STONE_BRICKS, Map.of(Blocks.CHISELED_POLISHED_BLACKSTONE,1));
        map.put(Blocks.CRACKED_STONE_BRICKS, Map.of(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS,1));
    });

private StoneToGildedCaveProcessor() {}

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        Map <Block, Integer> options = this.replacements.get(relativeBlockInfo.state().getBlock());
        if (options == null)
            return relativeBlockInfo;
        Block block = ListAndArrayUtils.getRandomFromWeightedMapI(options, randomsource);
        if (block == null) {
            return relativeBlockInfo;
        } else {
            BlockState blockstate = relativeBlockInfo.state();
            BlockState blockstate1 = block.defaultBlockState();
            if (blockstate.hasProperty(StairBlock.FACING)) {
                blockstate1 =  blockstate1.setValue(StairBlock.FACING,  blockstate.getValue(StairBlock.FACING));
            }

            if (blockstate.hasProperty(StairBlock.HALF)) {
                blockstate1 =  blockstate1.setValue(StairBlock.HALF,  blockstate.getValue(StairBlock.HALF));
            }

            if (blockstate.hasProperty(SlabBlock.TYPE)) {
                blockstate1 =  blockstate1.setValue(SlabBlock.TYPE,  blockstate.getValue(SlabBlock.TYPE));
            }

            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), blockstate1, relativeBlockInfo.nbt());
        }
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLACKSTONE_REPLACE;
    }
}
