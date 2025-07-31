package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;
import java.util.function.Supplier;

public class StoneToGildedCaveProcessor extends GildedCaveOreProcessor {
    public static final StoneToGildedCaveProcessor INSTANCE = new StoneToGildedCaveProcessor();

    public static final MapCodec<StoneToGildedCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.COBBLESTONE, blackstoneMap);
                map.put(Blocks.MOSSY_COBBLESTONE, blackstoneMap);
                map.put(Blocks.STONE, blackstoneMap);
                map.put(Blocks.GRANITE, blackstoneMap);
                map.put(Blocks.DIORITE, blackstoneMap);});


    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
