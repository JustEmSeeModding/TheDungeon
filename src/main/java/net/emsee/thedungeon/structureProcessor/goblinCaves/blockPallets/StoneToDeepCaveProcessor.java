package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPallets;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BlockPalletReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;
import java.util.function.Supplier;

public class StoneToDeepCaveProcessor extends BlockPalletReplacementProcessor {
    public static final StoneToDeepCaveProcessor INSTANCE = new StoneToDeepCaveProcessor();

    public static final MapCodec<StoneToDeepCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    protected final WeightedMap.Int<ReplaceInstance> defaultMap =
            Util.make(new WeightedMap.Int<>(), (map) -> {
                map.put(new ReplaceInstance(() -> Blocks.DEEPSLATE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)), 150);
                map.put(new ReplaceInstance(() -> Blocks.DEEPSLATE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)), 150);
                map.put(new ReplaceInstance(() -> Blocks.DEEPSLATE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)), 150);
                map.put(new ReplaceInstance(() -> Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)), 100);
                map.put(new ReplaceInstance(() -> Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)), 100);
                map.put(new ReplaceInstance(() -> Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)), 100);
                map.put(new ReplaceInstance(Blocks.COBBLED_DEEPSLATE::defaultBlockState), 300);
                map.put(new ReplaceInstance(Blocks.SMOOTH_BASALT::defaultBlockState), 200);
                map.put(new ReplaceInstance(Blocks.BEDROCK::defaultBlockState), 200);
                map.put(new ReplaceInstance(Blocks.DEEPSLATE_GOLD_ORE::defaultBlockState), 11);
                map.put(new ReplaceInstance(Blocks.DEEPSLATE_COAL_ORE::defaultBlockState), 4);
                map.put(new ReplaceInstance(Blocks.DEEPSLATE_COPPER_ORE::defaultBlockState), 1);
                map.put(new ReplaceInstance(Blocks.DEEPSLATE_IRON_ORE::defaultBlockState), 3);
                map.put(new ReplaceInstance(Blocks.DEEPSLATE_DIAMOND_ORE::defaultBlockState), 2);
                map.put(new ReplaceInstance(() -> ModBlocks.DEEPSLATE_PYRITE_ORE.get().defaultBlockState()), 4);
                map.put(new ReplaceInstance(() -> ModBlocks.INFUSED_DEEPSLATE.get().defaultBlockState()), 1);
            });

    protected final Map<Block, WeightedMap.Int<ReplaceInstance>> replacements =
            Util.make(Maps.newHashMap(), (map) -> {
                map.put(Blocks.STONE, defaultMap);
                map.put(Blocks.GRANITE, defaultMap);
                map.put(Blocks.DIORITE, defaultMap);
            });

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements() {
        return replacements;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
