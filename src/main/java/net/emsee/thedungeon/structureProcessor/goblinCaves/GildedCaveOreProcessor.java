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
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class GildedCaveOreProcessor extends StructureProcessor {
    public static final GildedCaveOreProcessor INSTANCE = new GildedCaveOreProcessor();

    public static final MapCodec<GildedCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final Map<Block, Integer> defaultMap = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.BLACKSTONE, 10);
        map.put(Blocks.GILDED_BLACKSTONE, 1);
    });

    private final Map<Block, Map<Block, Integer>> replacements = Util.make(Maps.newHashMap(), (map) -> {
        map.put(Blocks.BLACKSTONE, defaultMap);
    });

    /**
 * Constructs a singleton instance of GildedCaveOreProcessor.
 *
 * This private constructor enforces the singleton pattern, preventing external instantiation.
 */
private GildedCaveOreProcessor() {}

    /**
     * Replaces certain blocks during structure placement with weighted alternatives, preserving relevant block state properties.
     *
     * If the block at the given position has defined replacement options, randomly selects a replacement based on weights and applies it, copying over stair and slab properties if present. If no replacement is applicable, returns the original block info unchanged.
     *
     * @return a new StructureBlockInfo with the replaced block state if applicable; otherwise, the original StructureBlockInfo
     */
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
