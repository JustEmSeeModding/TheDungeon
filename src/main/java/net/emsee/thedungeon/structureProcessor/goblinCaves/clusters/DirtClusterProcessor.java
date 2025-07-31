package net.emsee.thedungeon.structureProcessor.goblinCaves.clusters;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.OrganicClusterProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class DirtClusterProcessor extends OrganicClusterProcessor {
    public static final DirtClusterProcessor INSTANCE = new DirtClusterProcessor();

    public static final MapCodec<DirtClusterProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected long getBaseSeed() {
        return 75975735;
    }


    @Override
    protected int getBaseClusterRadius() {
        return 4;
    }

    @Override
    protected float getClusterSizeVariation() {
        return .7f;
    }

    @Override
    protected float getClusterDensity() {
        return .9f;
    }

    @Override
    protected float getClusterEdgeSmoothness() {
        return .8f;
    }

    protected final WeightedMap.Int<ReplaceInstance> basicMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 4);
                map.put(new ReplaceInstance(Blocks.DIRT::defaultBlockState), 1);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, basicMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
