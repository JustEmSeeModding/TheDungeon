package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class StoneToIceCaveProcessor extends BlockPalletReplacementProcessor {
    public static final StoneToIceCaveProcessor INSTANCE = new StoneToIceCaveProcessor();

    public static final MapCodec<StoneToIceCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.PACKED_ICE::defaultBlockState), 10);
                map.put(new ReplaceInstance(Blocks.BLUE_ICE::defaultBlockState), 7);
                map.put(new ReplaceInstance(Blocks.SNOW_BLOCK::defaultBlockState), 10);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, defaultMap);
                map.put(Blocks.GRANITE, defaultMap);
                map.put(Blocks.DIORITE, defaultMap);
            });


    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
