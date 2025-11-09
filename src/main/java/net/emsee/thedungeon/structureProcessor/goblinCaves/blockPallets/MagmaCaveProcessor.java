package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class MagmaCaveProcessor extends BlockPalletReplacementProcessor {
    public static final MagmaCaveProcessor INSTANCE = new MagmaCaveProcessor();

    public static final MapCodec<MagmaCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> blackstoneMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.BLACKSTONE::defaultBlockState), 50);
                map.put(new ReplaceInstance(Blocks.MAGMA_BLOCK::defaultBlockState), 1);
                map.put(new ReplaceInstance(Blocks.SMOOTH_BASALT::defaultBlockState), 4);
            });

    protected final WeightedMap.Int<ReplaceInstance> veinMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.RAW_GOLD_BLOCK::defaultBlockState), 2);
                map.put(new ReplaceInstance(Blocks.GILDED_BLACKSTONE::defaultBlockState), 3);
            });


    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.BLACKSTONE, blackstoneMap);
                map.put(Blocks.RAW_GOLD_BLOCK, veinMap);
            });


    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
