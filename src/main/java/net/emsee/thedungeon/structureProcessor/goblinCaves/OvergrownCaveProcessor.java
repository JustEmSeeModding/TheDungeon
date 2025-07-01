package net.emsee.thedungeon.structureProcessor.goblinCaves;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BasicReplacementProcessor;
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

public class OvergrownCaveProcessor extends BasicReplacementProcessor {
    public static final OvergrownCaveProcessor INSTANCE = new OvergrownCaveProcessor();

    public static final MapCodec<OvergrownCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    private final WeightedMap.Int<Supplier<BlockState>> defaultStoneMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.STONE::defaultBlockState, 325);
                map.put(Blocks.ANDESITE::defaultBlockState, 325);
                map.put(Blocks.CLAY::defaultBlockState, 50);
                map.put(Blocks.COBBLESTONE::defaultBlockState, 275);
                map.put(Blocks.MOSSY_COBBLESTONE::defaultBlockState, 100);
                map.put(Blocks.TUFF::defaultBlockState, 375);
                map.put(Blocks.GOLD_ORE::defaultBlockState, 10);
                map.put(Blocks.COAL_ORE::defaultBlockState, 7);
                map.put(Blocks.COPPER_ORE::defaultBlockState, 2);
                map.put(Blocks.IRON_ORE::defaultBlockState, 4);
                map.put(Blocks.DIAMOND_ORE::defaultBlockState, 1);
                map.put(() -> ModBlocks.PYRITE_ORE.get().defaultBlockState(), 4);
                map.put(() -> ModBlocks.INFUSED_STONE.get().defaultBlockState(), 2);
                map.put(() -> ModBlocks.INFUSED_CLAY.get().defaultBlockState(), 1);
            });

    private final WeightedMap.Int<Supplier<BlockState>> defaultGrassMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.GRASS_BLOCK::defaultBlockState, 100);
                map.put(Blocks.MOSS_BLOCK::defaultBlockState, 100);
            });

    private final WeightedMap.Int<Supplier<BlockState>> defaultDirtMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.DIRT::defaultBlockState, 100);
                map.put(Blocks.COARSE_DIRT::defaultBlockState, 100);
                map.put(Blocks.ROOTED_DIRT::defaultBlockState, 100);
                map.put(() -> ModBlocks.INFUSED_DIRT.get().defaultBlockState(), 1);
            });

    private final WeightedMap.Int<Supplier<BlockState>> defaultPlantMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(Blocks.AIR::defaultBlockState, 100);
                map.put(Blocks.SHORT_GRASS::defaultBlockState, 100);
                map.put(Blocks.MOSS_CARPET::defaultBlockState, 20);
                map.put(Blocks.FERN::defaultBlockState, 25);
                //map.put(Blocks.BROWN_MUSHROOM::defaultBlockState, 5);
                map.put(Blocks.AZURE_BLUET::defaultBlockState, 4);
                map.put(Blocks.BLUE_ORCHID::defaultBlockState, 5);
                map.put(Blocks.OXEYE_DAISY::defaultBlockState, 2);
                map.put(Blocks.CORNFLOWER::defaultBlockState, 5);
                map.put(Blocks.LILY_OF_THE_VALLEY::defaultBlockState, 3);
                map.put(Blocks.WHITE_TULIP::defaultBlockState, 2);
                map.put(Blocks.PINK_TULIP::defaultBlockState, 2);
                map.put(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 1), 1);
                map.put(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 2), 1);
                map.put(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 3), 1);
                map.put(()->Blocks.PINK_PETALS.defaultBlockState().setValue(PinkPetalsBlock.AMOUNT, 4), 1);
            });

    private final WeightedMap.Int<Supplier<BlockState>> defaultLeavesMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(() -> Blocks.OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 100);
                map.put(() -> Blocks.ACACIA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 100);
                map.put(() -> Blocks.DARK_OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 100);
                map.put(() -> Blocks.JUNGLE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 50);
                map.put(() -> Blocks.AZALEA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 20);
                map.put(() -> Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 5);
                map.put(() -> Blocks.MANGROVE_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 10);
                map.put(() -> Blocks.BIRCH_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 10);
            });

    private final Map<Block, WeightedMap.Int<Supplier<BlockState>>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, defaultStoneMap);
                map.put(Blocks.GRASS_BLOCK, defaultGrassMap);
                map.put(Blocks.DIRT, defaultDirtMap);
                map.put(Blocks.SHORT_GRASS, defaultPlantMap);
                map.put(Blocks.OAK_LEAVES, defaultLeavesMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
