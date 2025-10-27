package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class OvergrownCaveProcessor extends StoneCaveOreProcessor {
    public static final OvergrownCaveProcessor INSTANCE = new OvergrownCaveProcessor();

    public static final MapCodec<OvergrownCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> overgrownStoneMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 325);
                map.put(new ReplaceInstance(Blocks.ANDESITE::defaultBlockState), 325);
                map.put(new ReplaceInstance(Blocks.CLAY::defaultBlockState), 50);
                map.put(new ReplaceInstance(Blocks.COBBLESTONE::defaultBlockState), 275);
                map.put(new ReplaceInstance(Blocks.MOSSY_COBBLESTONE::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.TUFF::defaultBlockState), 375);
                map.put(new ReplaceInstance(Blocks.GOLD_ORE::defaultBlockState), 10);
                map.put(new ReplaceInstance(Blocks.COAL_ORE::defaultBlockState), 7);
                map.put(new ReplaceInstance(Blocks.COPPER_ORE::defaultBlockState), 2);
                map.put(new ReplaceInstance(() -> ModBlocks.PYRITE_ORE.get().defaultBlockState()), 4);
                map.put(new ReplaceInstance(() -> ModBlocks.INFUSED_STONE.get().defaultBlockState()), 2);
                map.put(new ReplaceInstance(() -> ModBlocks.INFUSED_CLAY.get().defaultBlockState()), 1);
            });

    protected final WeightedMap.Int<ReplaceInstance> grassMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(Blocks.GRASS_BLOCK::defaultBlockState), 500);
                map.put(new ReplaceInstance(Blocks.MOSS_BLOCK::defaultBlockState), 500);
                map.put(new ReplaceInstance(() -> ModBlocks.INFUSED_GRASS_BLOCK.get().defaultBlockState()), 1);
            });

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
                map.put(new ReplaceInstance(Blocks.SHORT_GRASS::defaultBlockState), 100);
                map.put(new ReplaceInstance(Blocks.MOSS_CARPET::defaultBlockState), 20);
                map.put(new ReplaceInstance(Blocks.FERN::defaultBlockState), 25);
                map.put(new ReplaceInstance(Blocks.AZURE_BLUET::defaultBlockState), 4);
                map.put(new ReplaceInstance(Blocks.BLUE_ORCHID::defaultBlockState), 5);
                map.put(new ReplaceInstance(Blocks.OXEYE_DAISY::defaultBlockState), 2);
                map.put(new ReplaceInstance(Blocks.CORNFLOWER::defaultBlockState), 5);
                map.put(new ReplaceInstance(Blocks.LILY_OF_THE_VALLEY::defaultBlockState), 3);
                map.put(new ReplaceInstance(Blocks.WHITE_TULIP::defaultBlockState), 2);
                map.put(new ReplaceInstance(Blocks.PINK_TULIP::defaultBlockState), 2);
                map.put(new ReplaceInstance(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 1)), 1);
                map.put(new ReplaceInstance(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 2)), 1);
                map.put(new ReplaceInstance(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 3)), 1);
                map.put(new ReplaceInstance(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 4)), 1);
            });

    protected final WeightedMap.Int<ReplaceInstance> leavesMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(() -> Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 100);
                map.put(new ReplaceInstance(() -> Blocks.ACACIA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 100);
                map.put(new ReplaceInstance(() -> Blocks.DARK_OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 100);
                map.put(new ReplaceInstance(() -> Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 50);
                map.put(new ReplaceInstance(() -> Blocks.AZALEA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 20);
                map.put(new ReplaceInstance(() -> Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 5);
                map.put(new ReplaceInstance(() -> Blocks.MANGROVE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 10);
                map.put(new ReplaceInstance(() -> Blocks.BIRCH_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 10);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, overgrownStoneMap);
                map.put(Blocks.DIORITE, dioriteMap);
                map.put(Blocks.GRANITE, graniteMap);
                map.put(Blocks.GRASS_BLOCK, grassMap);
                map.put(Blocks.DIRT, dirtMap);
                map.put(Blocks.SHORT_GRASS, plantMap);
                map.put(Blocks.OAK_LEAVES, leavesMap);
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
