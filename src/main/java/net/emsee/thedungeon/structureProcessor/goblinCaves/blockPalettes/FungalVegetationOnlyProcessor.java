package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

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

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.BROWN_MUSHROOM,
                    Util.make(new WeightedMap.Int<>(), (plantMap) -> {
                        plantMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 100);
                        plantMap.put(new ReplaceInstance(Blocks.BROWN_MUSHROOM::defaultBlockState), 10);
                        plantMap.put(new ReplaceInstance(Blocks.RED_MUSHROOM::defaultBlockState), 1);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
