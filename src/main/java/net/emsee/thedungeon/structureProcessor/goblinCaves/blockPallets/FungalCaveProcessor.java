package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class FungalCaveProcessor extends StoneCaveOreProcessor {
    public static final FungalCaveProcessor INSTANCE = new FungalCaveProcessor();

    public static final MapCodec<FungalCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> dirtMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.DIRT::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.COARSE_DIRT::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.ROOTED_DIRT::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.PACKED_MUD::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.LIGHT_GRAY_TERRACOTTA::defaultBlockState), 30);
                map.put(new ReplaceInstance(Blocks.TERRACOTTA::defaultBlockState), 10);
                map.put(new ReplaceInstance(() -> ModBlocks.INFUSED_DIRT.get().defaultBlockState()), 1);
            });

    protected final WeightedMap.Int<ReplaceInstance> plantMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.BROWN_MUSHROOM::defaultBlockState), 10);
                map.put(new ReplaceInstance(Blocks.RED_MUSHROOM::defaultBlockState), 1);
            });


    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, stoneMap);
                //map.put(Blocks.MYCELIUM, myceliumMap);
                map.put(Blocks.DIRT, dirtMap);
                map.put(Blocks.DIORITE, dioriteMap);
                map.put(Blocks.GRANITE, graniteMap);
                map.put(Blocks.BROWN_MUSHROOM, plantMap);
                map.put(Blocks.RAW_IRON_BLOCK, ironOreVeinMap);
                map.put(Blocks.RAW_COPPER_BLOCK, copperOreVeinMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
