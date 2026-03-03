package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;

import java.util.Map;

public class OvergrownCaveProcessor extends StoneCaveOreProcessor {
    public static final OvergrownCaveProcessor INSTANCE = new OvergrownCaveProcessor();

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(super.createReplacements(), (map) -> {
            map.put(Blocks.STONE,
                    Util.make(new WeightedMap.Int<>(), (stoneMap) -> {
                        stoneMap.put(new ReplaceInstance(Blocks.STONE::defaultBlockState), 325);
                        stoneMap.put(new ReplaceInstance(Blocks.ANDESITE::defaultBlockState), 325);
                        stoneMap.put(new ReplaceInstance(Blocks.CLAY::defaultBlockState), 50);
                        stoneMap.put(new ReplaceInstance(Blocks.COBBLESTONE::defaultBlockState), 275);
                        stoneMap.put(new ReplaceInstance(Blocks.MOSSY_COBBLESTONE::defaultBlockState), 100);
                        stoneMap.put(new ReplaceInstance(Blocks.TUFF::defaultBlockState), 375);
                        stoneMap.put(new ReplaceInstance(()-> ModBlocks.GILDREAN_BLOCKS.ORE.get().defaultBlockState()), 10);
                        //stoneMap.put(new ReplaceInstance(Blocks.COAL_ORE::defaultBlockState), 7);
                        //stoneMap.put(new ReplaceInstance(Blocks.COPPER_ORE::defaultBlockState), 2);
                        stoneMap.put(new ReplaceInstance(() -> ModBlocks.PYRITE_BLOCKS.ORE.get().defaultBlockState()), 4);
                        stoneMap.put(new ReplaceInstance(() -> ModBlocks.INFUSED_STONE.get().defaultBlockState()), 2);
                        stoneMap.put(new ReplaceInstance(() -> ModBlocks.INFUSED_CLAY.get().defaultBlockState()), 1);
                    }));
            map.put(Blocks.GRASS_BLOCK,
                    Util.make(new WeightedMap.Int<>(), (grassMap) -> {
                        grassMap.put(new ReplaceInstance(Blocks.GRASS_BLOCK::defaultBlockState), 500);
                        grassMap.put(new ReplaceInstance(Blocks.MOSS_BLOCK::defaultBlockState), 500);
                        grassMap.put(new ReplaceInstance(() -> ModBlocks.INFUSED_GRASS_BLOCK.get().defaultBlockState()), 1);
                    }));
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
            map.put(Blocks.SHORT_GRASS,
                    Util.make(new WeightedMap.Int<>(), (plantMap) -> {
                        plantMap.put(new ReplaceInstance(Blocks.AIR::defaultBlockState), 100);
                        plantMap.put(new ReplaceInstance(Blocks.SHORT_GRASS::defaultBlockState), 100);
                        plantMap.put(new ReplaceInstance(Blocks.MOSS_CARPET::defaultBlockState), 20);
                        plantMap.put(new ReplaceInstance(Blocks.FERN::defaultBlockState), 25);
                        plantMap.put(new ReplaceInstance(Blocks.AZURE_BLUET::defaultBlockState), 4);
                        plantMap.put(new ReplaceInstance(Blocks.BLUE_ORCHID::defaultBlockState), 5);
                        plantMap.put(new ReplaceInstance(Blocks.OXEYE_DAISY::defaultBlockState), 2);
                        plantMap.put(new ReplaceInstance(Blocks.CORNFLOWER::defaultBlockState), 5);
                        plantMap.put(new ReplaceInstance(Blocks.LILY_OF_THE_VALLEY::defaultBlockState), 3);
                        plantMap.put(new ReplaceInstance(Blocks.WHITE_TULIP::defaultBlockState), 2);
                        plantMap.put(new ReplaceInstance(Blocks.PINK_TULIP::defaultBlockState), 2);
                        plantMap.put(new ReplaceInstance(() -> Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 1)), 1);
                        plantMap.put(new ReplaceInstance(() -> Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 2)), 1);
                        plantMap.put(new ReplaceInstance(() -> Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 3)), 1);
                        plantMap.put(new ReplaceInstance(() -> Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 4)), 1);
                    }));
            map.put(Blocks.OAK_LEAVES,
                    Util.make(new WeightedMap.Int<>(), (leavesMap) -> {
                        leavesMap.put(new ReplaceInstance(() -> Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 100);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.ACACIA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 100);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.DARK_OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 100);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 50);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.AZALEA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 20);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 5);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.MANGROVE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 10);
                        leavesMap.put(new ReplaceInstance(() -> Blocks.BIRCH_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)), 10);
                    }));
        });
    }
}
