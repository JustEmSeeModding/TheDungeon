package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BasicReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class StoneToIceCaveProcessor extends BasicReplacementProcessor {
    public static final StoneToIceCaveProcessor INSTANCE = new StoneToIceCaveProcessor();

    public static final MapCodec<StoneToIceCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private StoneToIceCaveProcessor() {}

    private WeightedMap.Int<BlockState> defaultMap() {
        return Util.make(new WeightedMap.Int<>(), (map) -> {
            map.put(Blocks.PACKED_ICE.defaultBlockState(), 10);
            map.put(Blocks.BLUE_ICE.defaultBlockState(), 7);
        });
    }


    protected Map<Block, WeightedMap.Int<BlockState>> replacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE, defaultMap());
        });
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
