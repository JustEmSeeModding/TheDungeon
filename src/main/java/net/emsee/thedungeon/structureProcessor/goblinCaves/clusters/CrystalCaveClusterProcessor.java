package net.emsee.thedungeon.structureProcessor.goblinCaves.clusters;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.entity.custom.CrystalGolemEntity;
import net.emsee.thedungeon.structureProcessor.OrganicClusterProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class CrystalCaveClusterProcessor extends OrganicClusterProcessor {
    public static final CrystalCaveClusterProcessor INSTANCE = new CrystalCaveClusterProcessor();

    public static final MapCodec<CrystalCaveClusterProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected long getBaseSeed() {
        return 985675735;
    }

    @Override
    protected boolean getIsSeparateSeedPerReplacementBlock() {
        return false;
    }

    @Override
    protected int getBaseClusterRadius() {
        return 3;
    }

    @Override
    protected float getClusterSizeVariation() {
        return .6f;
    }

    @Override
    protected float getClusterDensity() {
        return 1.5f;
    }

    @Override
    protected float getClusterEdgeSmoothness() {
        return .2f;
    }

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.CALCITE,
                    Util.make(new WeightedMap.Int<>(), (calciteMap) -> {
                        calciteMap.put(new ReplaceInstance(Blocks.CALCITE::defaultBlockState), 600);
                        calciteMap.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 300);

                        calciteMap.put(new ReplaceInstance(Blocks.AMETHYST_BLOCK::defaultBlockState), CrystalGolemEntity.Variant.AMETHYST.getWeight());
                        calciteMap.put(new ReplaceInstance(() -> ModBlocks.ROSELITH_BLOCK.get().defaultBlockState()), CrystalGolemEntity.Variant.ROSELITH.getWeight());
                        calciteMap.put(new ReplaceInstance(() -> ModBlocks.GARNETORE_BLOCK.get().defaultBlockState()), CrystalGolemEntity.Variant.GARNETORE.getWeight());
                        calciteMap.put(new ReplaceInstance(() -> ModBlocks.VERDATITE_BLOCK.get().defaultBlockState()), CrystalGolemEntity.Variant.VERDANTITE.getWeight());
                        calciteMap.put(new ReplaceInstance(() -> ModBlocks.LUMANITE_BLOCK.get().defaultBlockState()), CrystalGolemEntity.Variant.LUMANITE.getWeight());
                    }));
        });
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
