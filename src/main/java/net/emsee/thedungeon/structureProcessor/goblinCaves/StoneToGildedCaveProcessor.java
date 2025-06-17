package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BasicReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;

public class StoneToGildedCaveProcessor extends BasicReplacementProcessor {
    public static final StoneToGildedCaveProcessor INSTANCE = new StoneToGildedCaveProcessor();

    public static final MapCodec<StoneToGildedCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private WeightedMap.Int<BlockState> defaultMap() {
        return Util.make(new WeightedMap.Int<>(), (map) -> {
            map.put(Blocks.BLACKSTONE.defaultBlockState(), 5);
            map.put(Blocks.GILDED_BLACKSTONE.defaultBlockState(), 1);
        });
    }


    protected Map<Block, WeightedMap.Int<BlockState>> replacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.COBBLESTONE, defaultMap());
            map.put(Blocks.MOSSY_COBBLESTONE, defaultMap());
            map.put(Blocks.STONE, defaultMap());
            map.put(Blocks.STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState(), 1)));
            map.put(Blocks.COBBLESTONE_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_STAIRS.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_COBBLESTONE_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_STAIRS.defaultBlockState(), 1)));
            map.put(Blocks.STONE_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_STAIRS.defaultBlockState(), 1)));
            map.put(Blocks.STONE_BRICK_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_STONE_BRICK_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS.defaultBlockState(), 1)));
            map.put(Blocks.COBBLESTONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_SLAB.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_COBBLESTONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_SLAB.defaultBlockState(), 1)));
            map.put(Blocks.SMOOTH_STONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_SLAB.defaultBlockState(), 1)));
            map.put(Blocks.STONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_SLAB.defaultBlockState(), 1)));
            map.put(Blocks.STONE_BRICK_SLAB, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_STONE_BRICK_SLAB, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB.defaultBlockState(), 1)));
            map.put(Blocks.STONE_BRICK_WALL, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_WALL.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_STONE_BRICK_WALL, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_WALL.defaultBlockState(), 1)));
            map.put(Blocks.COBBLESTONE_WALL, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_WALL.defaultBlockState(), 1)));
            map.put(Blocks.MOSSY_COBBLESTONE_WALL, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_WALL.defaultBlockState(), 1)));
            map.put(Blocks.CHISELED_STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.CHISELED_POLISHED_BLACKSTONE.defaultBlockState(), 1)));
            map.put(Blocks.CRACKED_STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState(), 1)));
        });
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
