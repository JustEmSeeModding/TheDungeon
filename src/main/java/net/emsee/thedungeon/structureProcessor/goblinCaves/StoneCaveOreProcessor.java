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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Map;

public class StoneCaveOreProcessor extends StructureProcessor {
    public static final StoneCaveOreProcessor INSTANCE = new StoneCaveOreProcessor();

    public static final MapCodec<StoneCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final Map<Block, Integer> defaultMap = Map.of(
            Blocks.STONE,250,
            Blocks.ANDESITE,250,
            Blocks.COBBLESTONE,250,
            Blocks.TUFF,250,
            Blocks.GOLD_ORE, 10,
            Blocks.COAL_ORE, 6,
            Blocks.IRON_ORE, 4,
            Blocks.DIAMOND_ORE, 2);

    private final Map<Block, Map<Block, Integer>> replacements = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.STONE, defaultMap);
    });

    private StoneCaveOreProcessor() {}

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        Block oldBlock = relativeBlockInfo.state().getBlock();
        Map <Block, Integer> options = this.replacements.get(oldBlock);
        if (options == null)
            return relativeBlockInfo;
        Block block = ListAndArrayUtils.getRandomFromWeightedMapI(options, randomsource);
        if (block == null || block == oldBlock) {
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
