package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class FungalCaveProcessor extends StoneCaveOreProcessor {
    public static final FungalCaveProcessor INSTANCE = new FungalCaveProcessor();

    public static final MapCodec<FungalCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(super.createReplacements(), (map) -> {
            map.put(Blocks.DIRT,
                    Util.make(new WeightedMap.Int<>(), (dirtMap) -> {
                        dirtMap.put(new ReplaceInstance(Blocks.DIRT::defaultBlockState), 100);
                        dirtMap.put(new ReplaceInstance(Blocks.COARSE_DIRT::defaultBlockState), 100);
                        dirtMap.put(new ReplaceInstance(Blocks.ROOTED_DIRT::defaultBlockState), 100);
                        dirtMap.put(new ReplaceInstance(Blocks.PACKED_MUD::defaultBlockState), 100);
                        dirtMap.put(new ReplaceInstance(Blocks.LIGHT_GRAY_TERRACOTTA::defaultBlockState), 30);
                        dirtMap.put(new ReplaceInstance(Blocks.TERRACOTTA::defaultBlockState), 10);
                        dirtMap.put(new ReplaceInstance(() -> ModBlocks.INFUSED_DIRT.get().defaultBlockState()), 1);
                    }));
            map.put(Blocks.BROWN_MUSHROOM,
                    Util.make(new WeightedMap.Int<>(), (plantMap) -> {
                        plantMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 100);
                        plantMap.put(new ReplaceInstance(Blocks.BROWN_MUSHROOM::defaultBlockState), 10);
                        plantMap.put(new ReplaceInstance(Blocks.RED_MUSHROOM::defaultBlockState), 1);
                    }));
        });
    }
}
