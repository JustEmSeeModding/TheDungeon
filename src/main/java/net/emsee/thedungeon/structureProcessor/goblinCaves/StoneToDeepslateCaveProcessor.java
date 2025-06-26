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

public class StoneToDeepslateCaveProcessor extends BasicReplacementProcessor {
    public static final StoneToDeepslateCaveProcessor INSTANCE = new StoneToDeepslateCaveProcessor();

    public static final MapCodec<StoneToDeepslateCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final WeightedMap.Int<Supplier<BlockState>> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.DEEPSLATE::defaultBlockState, 750);
                map.put(Blocks.COBBLED_DEEPSLATE::defaultBlockState, 750);
                map.put(Blocks.DEEPSLATE_GOLD_ORE::defaultBlockState, 11);
                map.put(Blocks.DEEPSLATE_COAL_ORE::defaultBlockState, 4);
                map.put(Blocks.DEEPSLATE_COPPER_ORE::defaultBlockState, 1);
                map.put(Blocks.DEEPSLATE_IRON_ORE::defaultBlockState, 3);
                map.put(Blocks.DEEPSLATE_DIAMOND_ORE::defaultBlockState, 2);
                map.put(() -> ModBlocks.INFUSED_DEEPSLATE.get().defaultBlockState(), 1);
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
