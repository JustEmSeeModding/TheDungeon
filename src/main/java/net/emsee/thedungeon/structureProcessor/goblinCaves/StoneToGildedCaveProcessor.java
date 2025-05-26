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
        map.put(Blocks.SPRUCE_LOG, Map.of(Blocks.DARK_OAK_LOG,1));
        map.put(Blocks.SPRUCE_WOOD, Map.of(Blocks.DARK_OAK_WOOD,1));
        map.put(Blocks.STRIPPED_SPRUCE_LOG, Map.of(Blocks.STRIPPED_DARK_OAK_LOG,1));
        map.put(Blocks.STRIPPED_SPRUCE_WOOD, Map.of(Blocks.STRIPPED_DARK_OAK_WOOD,1));
        map.put(Blocks.SPRUCE_SLAB, Map.of(Blocks.DARK_OAK_SLAB,1));
        map.put(Blocks.SPRUCE_STAIRS, Map.of(Blocks.DARK_OAK_STAIRS,1));
        map.put(Blocks.SPRUCE_PLANKS, Map.of(Blocks.DARK_OAK_PLANKS,1));
        map.put(Blocks.SPRUCE_FENCE, Map.of(Blocks.DARK_OAK_FENCE,1));
        map.put(Blocks.SPRUCE_FENCE_GATE, Map.of(Blocks.DARK_OAK_FENCE_GATE,1));
        map.put(Blocks.SPRUCE_DOOR, Map.of(Blocks.DARK_OAK_DOOR,1));
        map.put(Blocks.SPRUCE_TRAPDOOR, Map.of(Blocks.DARK_OAK_TRAPDOOR,1));
        map.put(Blocks.SPRUCE_BUTTON, Map.of(Blocks.DARK_OAK_BUTTON,1));
        map.put(Blocks.SPRUCE_PRESSURE_PLATE, Map.of(Blocks.DARK_OAK_PRESSURE_PLATE,1));
        map.put(Blocks.SPRUCE_HANGING_SIGN, Map.of(Blocks.DARK_OAK_HANGING_SIGN,1));
        map.put(Blocks.SPRUCE_WALL_HANGING_SIGN, Map.of(Blocks.DARK_OAK_WALL_HANGING_SIGN,1));
        map.put(Blocks.SPRUCE_SIGN, Map.of(Blocks.DARK_OAK_SIGN,1));
        map.put(Blocks.SPRUCE_WALL_SIGN, Map.of(Blocks.DARK_OAK_WALL_SIGN,1));
    });

    /**
 * Constructs a singleton instance of the processor.
 *
 * This private constructor prevents external instantiation.
 */
private StoneToGildedCaveProcessor() {}

    /****
     * Processes a structure block during placement, replacing certain stone or spruce wood blocks with blackstone, gilded blackstone, or dark oak variants.
     *
     * If the block type has defined replacements, selects a replacement based on weighted randomness and preserves relevant block state properties such as orientation and slab/stair type.
     *
     * @return a new StructureBlockInfo with the replacement block state if applicable; otherwise, the original StructureBlockInfo
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
