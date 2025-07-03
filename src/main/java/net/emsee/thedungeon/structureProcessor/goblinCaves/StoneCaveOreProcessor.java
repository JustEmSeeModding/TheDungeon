package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BasicReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class StoneCaveOreProcessor extends BasicReplacementProcessor {
    public static final StoneCaveOreProcessor INSTANCE = new StoneCaveOreProcessor();

    public static final MapCodec<StoneCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final WeightedMap.Int<Supplier<BlockState>> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.STONE::defaultBlockState, 375);
                map.put(Blocks.ANDESITE::defaultBlockState, 375);
                map.put(Blocks.COBBLESTONE::defaultBlockState, 375);
                map.put(Blocks.TUFF::defaultBlockState, 375);
                map.put(Blocks.GOLD_ORE::defaultBlockState, 10);
                map.put(Blocks.COAL_ORE::defaultBlockState, 7);
                map.put(Blocks.COPPER_ORE::defaultBlockState, 2);
                map.put(Blocks.IRON_ORE::defaultBlockState, 4);
                map.put(Blocks.DIAMOND_ORE::defaultBlockState, 1);
                map.put(() -> ModBlocks.PYRITE_ORE.get().defaultBlockState(), 4);
                map.put(() -> ModBlocks.INFUSED_STONE.get().defaultBlockState(), 2);
                // TODO research if cyan Terracotta looks good? also concrete?
            });

    private final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, defaultMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
