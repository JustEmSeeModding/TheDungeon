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

public class StoneCaveOreProcessor extends BasicReplacementProcessor {
    public static final StoneCaveOreProcessor INSTANCE = new StoneCaveOreProcessor();

    public static final MapCodec<StoneCaveOreProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private WeightedMap.Int<Block> defaultMap() {
        return Util.make(new WeightedMap.Int<>(), (map) -> {
            map.put(Blocks.STONE, 450);
            map.put(Blocks.ANDESITE, 350);
            map.put(Blocks.COBBLESTONE, 350);
            map.put(Blocks.TUFF, 350);
            map.put(Blocks.GOLD_ORE, 10);
            map.put(Blocks.COAL_ORE, 6);
            map.put(Blocks.COPPER_ORE, 2);
            map.put(Blocks.IRON_ORE, 3);
            map.put(Blocks.DIAMOND_ORE, 1);
            map.put(ModBlocks.INFUSED_STONE.get(), 1);
        });
    }
    @Override
    protected Map<Block,WeightedMap.Int<Block>> replacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.STONE, defaultMap());
        });
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
