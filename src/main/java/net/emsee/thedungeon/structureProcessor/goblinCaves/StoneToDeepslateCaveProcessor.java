package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BasicReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class StoneToDeepslateCaveProcessor extends BasicReplacementProcessor {
    public static final StoneToDeepslateCaveProcessor INSTANCE = new StoneToDeepslateCaveProcessor();

    public static final MapCodec<StoneToDeepslateCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private WeightedMap.Int<Block> defaultMap() {
        return Util.make(new WeightedMap.Int<>(), (map) -> {
            map.put(Blocks.DEEPSLATE, 750);
            map.put(Blocks.COBBLED_DEEPSLATE, 750);
            map.put(Blocks.DEEPSLATE_GOLD_ORE, 10);
            map.put(Blocks.DEEPSLATE_COAL_ORE, 6);
            map.put(Blocks.DEEPSLATE_COPPER_ORE, 2);
            map.put(Blocks.DEEPSLATE_IRON_ORE, 3);
            map.put(Blocks.DEEPSLATE_DIAMOND_ORE, 1);
            map.put(ModBlocks.INFUSED_DEEPSLATE.get(), 1);
        });
    }

    @Override
    protected Map<Block, WeightedMap.Int<Block>> replacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE, defaultMap());
        });
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
