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

public class MagmaCaveProcessor extends BlockPaletteReplacementProcessor {
    public static final MagmaCaveProcessor INSTANCE = new MagmaCaveProcessor();

    public static final MapCodec<MagmaCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.BLACKSTONE,
                    Util.make(new WeightedMap.Int<>(), (blackstoneMap) -> {
                        blackstoneMap.put(new ReplaceInstance(Blocks.BLACKSTONE::defaultBlockState), 50);
                        blackstoneMap.put(new ReplaceInstance(Blocks.MAGMA_BLOCK::defaultBlockState), 1);
                        blackstoneMap.put(new ReplaceInstance(Blocks.NETHERRACK::defaultBlockState), 3);
                        blackstoneMap.put(new ReplaceInstance(Blocks.SMOOTH_BASALT::defaultBlockState), 4);
                        blackstoneMap.put(new ReplaceInstance(Blocks.OBSIDIAN::defaultBlockState), 6);
                    }));
            map.put(Blocks.RAW_GOLD_BLOCK,
                    Util.make(new WeightedMap.Int<>(), (goldVeinMap) -> {
                        goldVeinMap.put(new ReplaceInstance(Blocks.RAW_GOLD_BLOCK::defaultBlockState), 2);
                        goldVeinMap.put(new ReplaceInstance(Blocks.GILDED_BLACKSTONE::defaultBlockState), 3);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
