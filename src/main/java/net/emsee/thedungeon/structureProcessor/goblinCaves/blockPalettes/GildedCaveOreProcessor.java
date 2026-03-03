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


//TODO, add dungeon variant of gold
public class GildedCaveOreProcessor extends BlockPaletteReplacementProcessor {
    public static final GildedCaveOreProcessor INSTANCE = new GildedCaveOreProcessor();

    public static final MapCodec<GildedCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.BLACKSTONE,
                    Util.make(new WeightedMap.Int<>(), (blackstoneMap) -> {
                        blackstoneMap.put(new ReplaceInstance(Blocks.BLACKSTONE::defaultBlockState), 5);
                        blackstoneMap.put(new ReplaceInstance(() -> ModBlocks.INGILDERD_BLACKSTONE.get().defaultBlockState()), 1);
                    }));
            map.put(Blocks.RAW_GOLD_BLOCK,
                    Util.make(new WeightedMap.Int<>(), (goldVeinMap) -> {
                        goldVeinMap.put(new ReplaceInstance(() -> ModBlocks.GILDREAN_BLOCKS.RAW_BLOCK.get().defaultBlockState()), 2);
                        goldVeinMap.put(new ReplaceInstance(() -> ModBlocks.INGILDERD_BLACKSTONE.get().defaultBlockState()), 3);
                    }));
            map.put(ModBlocks.GILDREAN_BLOCKS.RAW_BLOCK.get(),
                    Util.make(new WeightedMap.Int<>(), (goldVeinMap) -> {
                        goldVeinMap.put(new ReplaceInstance(() -> ModBlocks.GILDREAN_BLOCKS.RAW_BLOCK.get().defaultBlockState()), 2);
                        goldVeinMap.put(new ReplaceInstance(() -> ModBlocks.INGILDERD_BLACKSTONE.get().defaultBlockState()), 3);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
