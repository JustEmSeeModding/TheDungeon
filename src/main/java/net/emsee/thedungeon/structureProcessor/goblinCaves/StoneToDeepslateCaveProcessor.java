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

public class StoneToDeepslateCaveProcessor extends StructureProcessor {
    public static final StoneToDeepslateCaveProcessor INSTANCE = new StoneToDeepslateCaveProcessor();

    public static final MapCodec<StoneToDeepslateCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);


    private final Map<Block, Integer> defaultMap = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.DEEPSLATE,500);
        map.put(Blocks.COBBLED_DEEPSLATE,500);
        map.put(Blocks.DEEPSLATE_GOLD_ORE, 10);
        map.put(Blocks.DEEPSLATE_COAL_ORE, 6);
        map.put(Blocks.DEEPSLATE_IRON_ORE, 4);
        map.put(Blocks.DEEPSLATE_DIAMOND_ORE, 2);
    });

    private final Map<Block, Map<Block, Integer>> replacements = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.STONE, defaultMap);
    });

    private StoneToDeepslateCaveProcessor() {}

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
