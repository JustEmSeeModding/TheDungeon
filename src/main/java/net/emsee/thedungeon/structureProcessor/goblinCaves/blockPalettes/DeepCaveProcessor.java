package net.emsee.thedungeon.structureProcessor.goblinCaves.blockPalettes;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.emsee.thedungeon.block.ModBlocks;
import net.emsee.thedungeon.structureProcessor.BlockPaletteReplacementProcessor;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.Map;

public class DeepCaveProcessor extends BlockPaletteReplacementProcessor {
    public static final DeepCaveProcessor INSTANCE = new DeepCaveProcessor();

    public static final MapCodec<DeepCaveProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    protected Map<Block, WeightedMap.Int<ReplaceInstance>> createReplacements() {
        return Util.make(Maps.newHashMap(), (map) -> {
            map.put(Blocks.DEEPSLATE,
                    Util.make(new WeightedMap.Int<>(), (deepslateMap) -> {
                        deepslateMap.put(new ReplaceInstance(() -> Blocks.DEEPSLATE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)), 150);
                        deepslateMap.put(new ReplaceInstance(() -> Blocks.DEEPSLATE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)), 150);
                        deepslateMap.put(new ReplaceInstance(() -> Blocks.DEEPSLATE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)), 150);
                        deepslateMap.put(new ReplaceInstance(() -> Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X)), 100);
                        deepslateMap.put(new ReplaceInstance(() -> Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)), 100);
                        deepslateMap.put(new ReplaceInstance(() -> Blocks.BASALT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z)), 100);
                        deepslateMap.put(new ReplaceInstance(Blocks.COBBLED_DEEPSLATE::defaultBlockState), 300);
                        deepslateMap.put(new ReplaceInstance(Blocks.SMOOTH_BASALT::defaultBlockState), 200);
                        deepslateMap.put(new ReplaceInstance(Blocks.BEDROCK::defaultBlockState), 200);
                        //deepslateMap.put(new ReplaceInstance(Blocks.DEEPSLATE_GOLD_ORE::defaultBlockState), 11);
                        deepslateMap.put(new ReplaceInstance(()->ModBlocks.GILDREAN_BLOCKS.DEEPSLATE_ORE.get().defaultBlockState()), 5);
                        //deepslateMap.put(new ReplaceInstance(Blocks.DEEPSLATE_IRON_ORE::defaultBlockState), 3);
                        //deepslateMap.put(new ReplaceInstance(Blocks.DEEPSLATE_DIAMOND_ORE::defaultBlockState), 2);
                        deepslateMap.put(new ReplaceInstance(ModBlocks.PYRITE_BLOCKS.DEEPSLATE_ORE.get()::defaultBlockState), 4);
                        deepslateMap.put(new ReplaceInstance(ModBlocks.INFUSED_DEEPSLATE.get()::defaultBlockState), 1);
                    }));
        });
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}
