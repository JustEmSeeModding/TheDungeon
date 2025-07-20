package net.emsee.thedungeon.structureProcessor.goblinCaves.Pallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;
import java.util.function.Supplier;

public class StoneToGildedCaveProcessor extends GildedCaveOreProcessor {
    public static final StoneToGildedCaveProcessor INSTANCE = new StoneToGildedCaveProcessor();

    public static final MapCodec<StoneToGildedCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.COBBLESTONE, blackstoneMap);
                map.put(Blocks.MOSSY_COBBLESTONE, blackstoneMap);
                map.put(Blocks.STONE, blackstoneMap);
                map.put(Blocks.GRANITE, blackstoneMap);
                map.put(Blocks.DIORITE, blackstoneMap);
                map.put(Blocks.STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICKS::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICKS::defaultBlockState, 1)));
                map.put(Blocks.COBBLESTONE_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_STAIRS::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_COBBLESTONE_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_STAIRS::defaultBlockState, 1)));
                map.put(Blocks.STONE_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_STAIRS::defaultBlockState, 1)));
                map.put(Blocks.STONE_BRICK_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_STONE_BRICK_STAIRS, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS::defaultBlockState, 1)));
                map.put(Blocks.COBBLESTONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_SLAB::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_COBBLESTONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_SLAB::defaultBlockState, 1)));
                map.put(Blocks.SMOOTH_STONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_SLAB::defaultBlockState, 1)));
                map.put(Blocks.STONE_SLAB, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_SLAB::defaultBlockState, 1)));
                map.put(Blocks.STONE_BRICK_SLAB, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_STONE_BRICK_SLAB, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_SLAB::defaultBlockState, 1)));
                map.put(Blocks.STONE_BRICK_WALL, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_WALL::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_STONE_BRICK_WALL, new WeightedMap.Int<>(Map.of(Blocks.POLISHED_BLACKSTONE_BRICK_WALL::defaultBlockState, 1)));
                map.put(Blocks.COBBLESTONE_WALL, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_WALL::defaultBlockState, 1)));
                map.put(Blocks.MOSSY_COBBLESTONE_WALL, new WeightedMap.Int<>(Map.of(Blocks.BLACKSTONE_WALL::defaultBlockState, 1)));
                map.put(Blocks.CHISELED_STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.CHISELED_POLISHED_BLACKSTONE::defaultBlockState, 1)));
                map.put(Blocks.CRACKED_STONE_BRICKS, new WeightedMap.Int<>(Map.of(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS::defaultBlockState, 1)));
            });


    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
