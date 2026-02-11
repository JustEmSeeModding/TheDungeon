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

public class StoneCaveOreProcessor extends BlockPaletteReplacementProcessor {
    public static final StoneCaveOreProcessor INSTANCE = new StoneCaveOreProcessor();

    public static final MapCodec<StoneCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE,
                    Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                        stoneMap.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 375);
                        stoneMap.put(new ReplaceInstance(Blocks.ANDESITE::defaultBlockState), 375);
                        stoneMap.put(new ReplaceInstance(Blocks.COBBLESTONE::defaultBlockState), 375);
                        stoneMap.put(new ReplaceInstance(Blocks.TUFF::defaultBlockState), 375);
                        stoneMap.put(new ReplaceInstance(Blocks.GOLD_ORE::defaultBlockState), 10);
                        stoneMap.put(new ReplaceInstance(Blocks.COAL_ORE::defaultBlockState), 7);
                        stoneMap.put(new ReplaceInstance(() -> ModBlocks.PYRITE_ORE.get().defaultBlockState()), 4);
                        stoneMap.put(new ReplaceInstance(() -> ModBlocks.INFUSED_STONE.get().defaultBlockState()), 2);
                    }));
            map.put(Blocks.GRANITE,
                    Util.make(new WeightedMap.Int<>(), (graniteMap) -> {
                        graniteMap.put(new ReplaceInstance(Blocks.GRANITE::defaultBlockState), 375);
                        graniteMap.put(new ReplaceInstance(Blocks.LIGHT_GRAY_TERRACOTTA::defaultBlockState), 175);
                        graniteMap.put(new ReplaceInstance(Blocks.TERRACOTTA::defaultBlockState), 175);
                    }));
            map.put(Blocks.DIORITE,
                    Util.make(new WeightedMap.Int<>(), (dioriteMap) -> {
                        dioriteMap.put(new ReplaceInstance(Blocks.DIORITE::defaultBlockState), 375);
                        dioriteMap.put(new ReplaceInstance(Blocks.CALCITE::defaultBlockState), 375);
                    }));
            map.put(Blocks.RAW_IRON_BLOCK,
                    Util.make(new WeightedMap.Int<>(), (ironOreVeinMap) -> {
                        ironOreVeinMap.put(new ReplaceInstance(Blocks.RAW_IRON_BLOCK::defaultBlockState), 150);
                        ironOreVeinMap.put(new ReplaceInstance(Blocks.IRON_ORE::defaultBlockState), 200);
                    }));
            map.put(Blocks.RAW_COPPER_BLOCK,
                    Util.make(new WeightedMap.Int<>(), (copperOreVeinMap) -> {
                        copperOreVeinMap.put(new ReplaceInstance(Blocks.RAW_COPPER_BLOCK::defaultBlockState), 150);
                        copperOreVeinMap.put(new ReplaceInstance(Blocks.COPPER_ORE::defaultBlockState), 200);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
