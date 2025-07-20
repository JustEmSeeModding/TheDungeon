package net.emsee.thedungeon.structureProcessor.goblinCaves.Pallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.PalletReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class StoneToIceCaveProcessor extends PalletReplacementProcessor {
    public static final StoneToIceCaveProcessor INSTANCE = new StoneToIceCaveProcessor();

    public static final MapCodec<StoneToIceCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<Supplier<BlockState>> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.PACKED_ICE::defaultBlockState, 10);
                map.put(Blocks.BLUE_ICE::defaultBlockState, 7);
                map.put(Blocks.SNOW_BLOCK::defaultBlockState, 10);
            });

    protected final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, defaultMap);
                map.put(Blocks.GRANITE, defaultMap);
                map.put(Blocks.DIORITE, defaultMap);
            });


    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
