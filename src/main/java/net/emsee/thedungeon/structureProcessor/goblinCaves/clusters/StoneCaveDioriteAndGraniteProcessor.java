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

public class StoneCaveDioriteAndGraniteProcessor extends OrganicClusterProcessor {
    public static final StoneCaveDioriteAndGraniteProcessor INSTANCE = new StoneCaveDioriteAndGraniteProcessor();

    public static final MapCodec<StoneCaveDioriteAndGraniteProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected long getBaseSeed() {
        return 186345735;
    }
    @Override
    protected int getBaseClusterRadius() {
        return 4;
    }
    @Override
    protected float getClusterDensity() {
        return .85f;
    }

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE,
                    Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                        stoneMap.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 4);
                        stoneMap.put(new ReplaceInstance(Blocks.DIORITE::defaultBlockState), 5);
                        stoneMap.put(new ReplaceInstance(Blocks.GRANITE::defaultBlockState), 5);
                    }));
        });
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
