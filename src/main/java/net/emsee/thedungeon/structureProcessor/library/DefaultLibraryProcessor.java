package net.emsee.thedungeon.structureProcessor.library;


import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class DefaultLibraryProcessor extends BlockPaletteReplacementProcessor {


    public static final DefaultLibraryProcessor INSTANCE = new DefaultLibraryProcessor();

    public static final MapCodec<DefaultLibraryProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private DefaultLibraryProcessor() {
    }


    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.CHISELED_BOOKSHELF,
                    Util.make(new WeightedMap.Int<>(), (bookshelfMap) -> {
                        bookshelfMap.put(new ReplaceInstance(Blocks.BOOKSHELF::defaultBlockState), 10);
                        bookshelfMap.put(new ReplaceInstance(Blocks.CHISELED_BOOKSHELF::defaultBlockState), 2);
                    }));
        });
    }


    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
