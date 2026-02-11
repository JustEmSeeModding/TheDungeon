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

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE, Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                stoneMap.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 4);
                stoneMap.put(new ReplaceInstance(Blocks.DIRT::defaultBlockState), 1);
            }));
        });
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
