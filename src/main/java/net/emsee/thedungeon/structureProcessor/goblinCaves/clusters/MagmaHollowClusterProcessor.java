package net.emsee.thedungeon.structureProcessor.goblinCaves.clusters;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.HollowOrganicClusterProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class MagmaHollowClusterProcessor extends HollowOrganicClusterProcessor {
    public static final MagmaHollowClusterProcessor INSTANCE = new MagmaHollowClusterProcessor();

    public static final MapCodec<MagmaHollowClusterProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected float getShellThickness() {
        return 2f;
    }

    @Override
    protected long getBaseSeed() {
        return 15567273;
    }


    @Override
    protected int getBaseClusterRadius() {
        return 12;
    }

    @Override
    protected float getClusterSizeVariation() {
        return .1f;
    }

    @Override
    protected float getClusterDensity() {
        return 1f;
    }

    @Override
    protected float getClusterEdgeSmoothness() {
        return 1f;
    }


    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE,
                    Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                        stoneMap.put(new ReplaceInstance(Blocks.MAGMA_BLOCK::defaultBlockState), 1);
                    }));
            map.put(Blocks.BLACKSTONE,
                    Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                        stoneMap.put(new ReplaceInstance(Blocks.MAGMA_BLOCK::defaultBlockState), 1);
                    }));
        });
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
