package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class StoneToIceCaveProcessor extends BlockPaletteReplacementProcessor {
    public static final StoneToIceCaveProcessor INSTANCE = new StoneToIceCaveProcessor();

    public static final MapCodec<StoneToIceCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        WeightedMap.Int<ReplaceInstance> defaultMap =
                Util.make(new WeightedMap.Int<>(), (map) -> {
                    map.put(new ReplaceInstance(Blocks.PACKED_ICE::defaultBlockState), 10);
                    map.put(new ReplaceInstance(Blocks.BLUE_ICE::defaultBlockState), 7);
                    map.put(new ReplaceInstance(Blocks.SNOW_BLOCK::defaultBlockState), 10);
                });

        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE, defaultMap);
            map.put(Blocks.GRANITE, defaultMap);
            map.put(Blocks.DIORITE, defaultMap);
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
