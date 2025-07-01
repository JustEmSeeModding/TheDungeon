package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Map;
import java.util.function.Supplier;

public abstract class BasicReplacementProcessor extends StructureProcessor {
    protected abstract Map<Block, WeightedMap.Int<Supplier<BlockState>>> getReplacements();

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        WeightedMap.Int<Supplier<BlockState>> options = this.getReplacements().get(relativeBlockInfo.state().getBlock());
        if (options == null)
            return relativeBlockInfo;
        BlockState newBlockstate = options.getRandom(randomsource).get();
        if (newBlockstate == null) {
            return relativeBlockInfo;
        } else {
            BlockState oldBlockstate = relativeBlockInfo.state();
            newBlockstate = copyProperties(oldBlockstate, newBlockstate);

            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
        }
    }

    protected BlockState copyProperties(BlockState from, BlockState to) {
        if (from.hasProperty(StairBlock.FACING) &&
                to.hasProperty(StairBlock.FACING)) {
            to = to.setValue(StairBlock.FACING, from.getValue(StairBlock.FACING));
        }
        if (from.hasProperty(StairBlock.HALF) &&
                to.hasProperty(StairBlock.HALF)) {
            to = to.setValue(StairBlock.HALF, from.getValue(StairBlock.HALF));
        }

        if (from.hasProperty(SlabBlock.TYPE) &&
                to.hasProperty(SlabBlock.TYPE)) {
            to = to.setValue(SlabBlock.TYPE, from.getValue(SlabBlock.TYPE));
        }

        if (from.hasProperty(RotatedPillarBlock.AXIS) &&
                to.hasProperty(RotatedPillarBlock.AXIS)) {
            to = to.setValue(RotatedPillarBlock.AXIS, from.getValue(RotatedPillarBlock.AXIS));
        }
        if (from.hasProperty(HorizontalDirectionalBlock.FACING) &&
                to.hasProperty(HorizontalDirectionalBlock.FACING)) {
            to = to.setValue(HorizontalDirectionalBlock.FACING, from.getValue(HorizontalDirectionalBlock.FACING));
        }

        return to;
    }
}
