package net.emsee.thedungeon.structureProcessor.goblinCaves.pallets;

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

public class BlackstoneToPlainStoneProcessor extends PalletReplacementProcessor {
    public static final BlackstoneToPlainStoneProcessor INSTANCE = new BlackstoneToPlainStoneProcessor();

    public static final MapCodec<BlackstoneToPlainStoneProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private BlackstoneToPlainStoneProcessor() {
    }

    protected final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.BLACKSTONE, Util.make(new WeightedMap.Int<>(), replace -> replace.put(Blocks.STONE::defaultBlockState, 1)));
            });

    @Override
    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
