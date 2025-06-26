package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BasicReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class GildedCaveOreProcessor extends BasicReplacementProcessor {
    public static final GildedCaveOreProcessor INSTANCE = new GildedCaveOreProcessor();

    public static final MapCodec<GildedCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final WeightedMap.Int<Supplier<BlockState>> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.BLACKSTONE::defaultBlockState, 5);
                map.put(Blocks.GILDED_BLACKSTONE::defaultBlockState, 1);
            });


    protected final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.BLACKSTONE, defaultMap);
            });


    @Override
    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
