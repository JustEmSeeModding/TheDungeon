package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class CrystalCaveProcessor extends BlockPalletReplacementProcessor {
    public static final CrystalCaveProcessor INSTANCE = new CrystalCaveProcessor();

    public static final MapCodec<CrystalCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.CALCITE::defaultBlockState), 150);
                map.put(new ReplaceInstance(Blocks.DIORITE::defaultBlockState), 50);
                map.put(new ReplaceInstance(Blocks.QUARTZ_BLOCK::defaultBlockState), 5);
            });

    protected final WeightedMap.Int<ReplaceInstance> stoneMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 180);
                map.put(new ReplaceInstance(Blocks.ANDESITE::defaultBlockState), 125);
                map.put(new ReplaceInstance(Blocks.COBBLESTONE::defaultBlockState), 125);
                map.put(new ReplaceInstance(Blocks.TUFF::defaultBlockState), 225);
                map.put(new ReplaceInstance(Blocks.DIAMOND_ORE::defaultBlockState), 40);
                map.put(new ReplaceInstance(Blocks.EMERALD_ORE::defaultBlockState), 20);
                map.put(new ReplaceInstance(Blocks.LAPIS_ORE::defaultBlockState), 15);
                map.put(new ReplaceInstance(() -> ModBlocks.PYRITE_ORE.get().defaultBlockState()), 45);
            });


    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, stoneMap);
                map.put(Blocks.CALCITE, defaultMap);
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
