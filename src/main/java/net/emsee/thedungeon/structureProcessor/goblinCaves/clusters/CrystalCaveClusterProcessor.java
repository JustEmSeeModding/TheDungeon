package net.emsee.thedungeon.structureProcessor.goblinCaves.clusters;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.OrganicClusterProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

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
        return 1.3f;
    }

    @Override
    protected float getClusterEdgeSmoothness() {
        return .2f;
    }

    //MAKE SURE ALL CRYSTAL BLOCKS WITH BUDS STAY LAST
    protected final WeightedMap.Int<ReplaceInstance> basicMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.CALCITE::defaultBlockState), 4);
                map.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 1);
                map.put(new ReplaceInstance(Blocks.PRISMARINE::defaultBlockState), 4);

                map.put(new ReplaceInstance(Blocks.AMETHYST_BLOCK::defaultBlockState), 5);
            });

    //IMPORTANT MAKE SURE WEIGHTS MATCH WITH THE ABOVE MAP
    protected final WeightedMap.Int<ReplaceInstance> clusterMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                final int[] curID = new int[]{0};
                basicMap.keySet().forEach(key -> {
                    if (key.stateSupplier.get().getBlock() == Blocks.AMETHYST_BLOCK)
                        map.put(new ReplaceInstance(Blocks.BUDDING_AMETHYST::defaultBlockState), basicMap.get(key));
                    else
                        map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState, curID[0]++), basicMap.get(key));
                });
                // total weight of all blocks without budding
                //map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 9);
                // weight of the rest, ORDER MATTERS
                //map.put(new ReplaceInstance(Blocks.BUDDING_AMETHYST::defaultBlockState), 5);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.CALCITE, basicMap);
                map.put(Blocks.AIR, clusterMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
