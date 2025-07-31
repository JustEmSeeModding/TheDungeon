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

public class BlackstoneToPlainStoneProcessor extends BlockPalletReplacementProcessor {
    public static final BlackstoneToPlainStoneProcessor INSTANCE = new BlackstoneToPlainStoneProcessor();

    public static final MapCodec<BlackstoneToPlainStoneProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.BLACKSTONE, Util.make(new WeightedMap.Int<>(), replace -> replace.put( new ReplaceInstance(Blocks.STONE::defaultBlockState), 1)));
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
