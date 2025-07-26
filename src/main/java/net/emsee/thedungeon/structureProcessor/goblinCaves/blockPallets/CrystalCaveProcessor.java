package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Predicate;

public class CrystalCaveProcessor extends BlockPalletReplacementProcessor {
    public static final CrystalCaveProcessor INSTANCE = new CrystalCaveProcessor();

    public static final MapCodec<CrystalCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.CALCITE::defaultBlockState), 150);
                map.put(new ReplaceInstance(Blocks.DIORITE::defaultBlockState), 50);
            });

    protected final WeightedMap.Int<ReplaceInstance> stoneMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 325);
                map.put(new ReplaceInstance(Blocks.ANDESITE::defaultBlockState), 475);
                map.put(new ReplaceInstance(Blocks.COBBLESTONE::defaultBlockState), 175);
                map.put(new ReplaceInstance(Blocks.TUFF::defaultBlockState), 475);
                // TODO research if cyan Terracotta looks good? also concrete?
            });

    protected final WeightedMap.Int<ReplaceInstance> clusterMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                for (Direction direction : Direction.values()) {
                    map.put(new ReplaceInstance(() -> Blocks.SMALL_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)), 4);
                    map.put(new ReplaceInstance(() -> Blocks.MEDIUM_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)), 3);
                    map.put(new ReplaceInstance(() -> Blocks.LARGE_AMETHYST_BUD.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)), 2);
                    map.put(new ReplaceInstance(() -> Blocks.AMETHYST_CLUSTER.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction)), 1);
                }
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, stoneMap);
                map.put(Blocks.CALCITE, defaultMap);
                map.put(Blocks.BUDDING_AMETHYST, clusterMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
