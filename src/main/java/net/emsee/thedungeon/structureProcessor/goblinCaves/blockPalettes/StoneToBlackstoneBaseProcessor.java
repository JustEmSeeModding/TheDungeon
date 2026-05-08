package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.Map;

public class StoneToBlackstoneBaseProcessor extends BlockPaletteReplacementProcessor {
    public static final StoneToBlackstoneBaseProcessor INSTANCE = new StoneToBlackstoneBaseProcessor();

    public static final MapCodec<StoneToBlackstoneBaseProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        WeightedMap.Int<ReplaceInstance> replaceMap = Util.make(new WeightedMap.Int<>(), blackstoneMap -> {
            blackstoneMap.put(new ReplaceInstance(Blocks.BLACKSTONE::defaultBlockState),1);
        });

        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.COBBLESTONE, replaceMap);
            map.put(Blocks.MOSSY_COBBLESTONE, replaceMap);
            map.put(Blocks.STONE, replaceMap);
            map.put(Blocks.GRANITE, replaceMap);
            map.put(Blocks.DIORITE, replaceMap);
        });
    }


    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
