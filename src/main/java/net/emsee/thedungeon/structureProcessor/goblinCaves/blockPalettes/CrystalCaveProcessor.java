package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class CrystalCaveProcessor extends BlockPaletteReplacementProcessor {
    public static final CrystalCaveProcessor INSTANCE = new CrystalCaveProcessor();

    public static final MapCodec<CrystalCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE,
                    Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                        stoneMap.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 180);
                        stoneMap.put(new ReplaceInstance(Blocks.ANDESITE::defaultBlockState), 125);
                        stoneMap.put(new ReplaceInstance(Blocks.COBBLESTONE::defaultBlockState), 125);
                        stoneMap.put(new ReplaceInstance(Blocks.TUFF::defaultBlockState), 225);
                        stoneMap.put(new ReplaceInstance(Blocks.DIAMOND_ORE::defaultBlockState), 70);
                        stoneMap.put(new ReplaceInstance(Blocks.EMERALD_ORE::defaultBlockState), 35);
                        stoneMap.put(new ReplaceInstance(Blocks.LAPIS_ORE::defaultBlockState), 25);
                    }));
            map.put(Blocks.CALCITE,
                    Util.make(new WeightedMap.Int<>(), (calciteMap) -> {
                        calciteMap.put(new ReplaceInstance(Blocks.CALCITE::defaultBlockState), 150);
                        calciteMap.put(new ReplaceInstance(Blocks.DIORITE::defaultBlockState), 50);
                        calciteMap.put(new ReplaceInstance(Blocks.QUARTZ_BLOCK::defaultBlockState), 5);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
