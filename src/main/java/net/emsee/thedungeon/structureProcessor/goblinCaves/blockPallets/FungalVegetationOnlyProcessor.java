package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class FungalVegetationOnlyProcessor extends FungalCaveProcessor {
    public static final FungalVegetationOnlyProcessor INSTANCE = new FungalVegetationOnlyProcessor();

    public static final MapCodec<FungalVegetationOnlyProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> plantMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.BROWN_MUSHROOM::defaultBlockState), 10);
                map.put(new ReplaceInstance(Blocks.RED_MUSHROOM::defaultBlockState), 1);
            });


    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.BROWN_MUSHROOM, plantMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
