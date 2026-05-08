package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class BlackstoneToPlainStoneProcessor extends BlockPaletteReplacementProcessor {
    public static final BlackstoneToPlainStoneProcessor INSTANCE = new BlackstoneToPlainStoneProcessor();

    public static final MapCodec<BlackstoneToPlainStoneProcessor> CODEC = MapCodec.unit(() -> INSTANCE);


    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.BLACKSTONE,
                    Util.make(new WeightedMap.Int<>(), blackstoneMap -> {
                        blackstoneMap.put( new ReplaceInstance(Blocks.STONE::defaultBlockState), 1);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
