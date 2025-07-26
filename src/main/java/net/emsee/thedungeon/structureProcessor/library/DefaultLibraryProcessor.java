package net.emsee.thedungeon.structureProcessor.library;


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

public class DefaultLibraryProcessor extends BlockPalletReplacementProcessor {


    public static final DefaultLibraryProcessor INSTANCE = new DefaultLibraryProcessor();

    public static final MapCodec<DefaultLibraryProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private DefaultLibraryProcessor() {
    }

    private final WeightedMap.Int<ReplaceInstance> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.BOOKSHELF::defaultBlockState), 10);
                map.put(new ReplaceInstance(Blocks.CHISELED_BOOKSHELF::defaultBlockState), 2);
            });

    private final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.CHISELED_BOOKSHELF, defaultMap);
            });


    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
