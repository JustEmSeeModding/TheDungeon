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

    private final Map<Block, Integer> defaultMap = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.STONE, 250);
        map.put(Blocks.ANDESITE, 250);
        map.put(Blocks.COBBLESTONE, 250);
        map.put(Blocks.TUFF, 250);
        map.put(Blocks.GOLD_ORE, 10);
        map.put(Blocks.COAL_ORE, 6);
        map.put(Blocks.COPPER_ORE, 2);
        map.put(Blocks.IRON_ORE, 3);
        map.put(Blocks.DIAMOND_ORE,1);
    });

    private final Map<Block, Map<Block, Integer>> replacements = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.STONE, defaultMap);
    });

    /****
 * Constructs a singleton instance of the StoneCaveOreProcessor.
 *
 * This private constructor prevents external instantiation.
 */
private StoneCaveOreProcessor() {}

    /**
     * Replaces certain blocks during structure placement with randomly selected alternatives based on weighted probabilities.
     *
     * If the original block has defined replacement options, selects a replacement block according to configured weights and preserves relevant block state properties (such as stair orientation and slab type). Returns the original block info if no replacement is applicable.
     *
     * @return a new StructureBlockInfo with the replacement block state, or the original if no replacement occurs
     */
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
