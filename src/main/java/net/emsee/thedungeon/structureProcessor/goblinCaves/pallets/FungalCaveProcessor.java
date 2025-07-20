package net.emsee.thedungeon.structureProcessor.goblinCaves.pallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class FungalCaveProcessor extends StoneCaveOreProcessor {
    public static final FungalCaveProcessor INSTANCE = new FungalCaveProcessor();

    public static final MapCodec<FungalCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    /*protected final WeightedMap.Int<Supplier<BlockState>> overgrownStoneMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.STONE::defaultBlockState, 325);
                map.put(Blocks.ANDESITE::defaultBlockState, 325);
                map.put(Blocks.CLAY::defaultBlockState, 50);
                map.put(Blocks.COBBLESTONE::defaultBlockState, 275);
                map.put(Blocks.MOSSY_COBBLESTONE::defaultBlockState, 100);
                map.put(Blocks.TUFF::defaultBlockState, 375);
                map.put(Blocks.GOLD_ORE::defaultBlockState, 10);
                map.put(Blocks.COAL_ORE::defaultBlockState, 7);
                map.put(Blocks.COPPER_ORE::defaultBlockState, 2);
                map.put(Blocks.IRON_ORE::defaultBlockState, 4);
                map.put(Blocks.DIAMOND_ORE::defaultBlockState, 1);
                map.put(() -> ModBlocks.PYRITE_ORE.get().defaultBlockState(), 4);
                map.put(() -> ModBlocks.INFUSED_STONE.get().defaultBlockState(), 2);
                map.put(() -> ModBlocks.INFUSED_CLAY.get().defaultBlockState(), 1);
            });*/

    /*protected final WeightedMap.Int<Supplier<BlockState>> MyceliumMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.MYCELIUM::defaultBlockState, 500);
            });*/

    protected final WeightedMap.Int<Supplier<BlockState>> dirtMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.DIRT::defaultBlockState, 100);
                map.put(Blocks.COARSE_DIRT::defaultBlockState, 100);
                map.put(Blocks.ROOTED_DIRT::defaultBlockState, 100);
                map.put(Blocks.PACKED_MUD::defaultBlockState, 100);
                map.put(Blocks.LIGHT_GRAY_TERRACOTTA::defaultBlockState, 30);
                map.put(Blocks.TERRACOTTA::defaultBlockState, 10);
                map.put(() -> ModBlocks.INFUSED_DIRT.get().defaultBlockState(), 1);
            });

    protected final WeightedMap.Int<Supplier<BlockState>> plantMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.AIR::defaultBlockState, 100);
                map.put(Blocks.BROWN_MUSHROOM::defaultBlockState, 10);
                map.put(Blocks.RED_MUSHROOM::defaultBlockState, 1);
            });


    protected final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, stoneMap);
                //map.put(Blocks.MYCELIUM, myceliumMap);
                map.put(Blocks.DIRT, dirtMap);
                map.put(Blocks.BROWN_MUSHROOM, plantMap);
                map.put(Blocks.RAW_IRON_BLOCK, ironOreVeinMap);
                map.put(Blocks.RAW_COPPER_BLOCK, copperOreVeinMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
