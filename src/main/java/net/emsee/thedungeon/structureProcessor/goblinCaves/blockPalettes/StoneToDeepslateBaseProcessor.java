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

public class StoneToDeepslateBaseProcessor extends BlockPaletteReplacementProcessor {
    public static final StoneToDeepslateBaseProcessor INSTANCE = new StoneToDeepslateBaseProcessor();

    public static final MapCodec<StoneToDeepslateBaseProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        WeightedMap.Int<ReplaceInstance> defaultMap =
                Util.make(new WeightedMap.Int<>(), (map) -> {
                    map.put(new ReplaceInstance(Blocks.DEEPSLATE::defaultBlockState), 1);
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
