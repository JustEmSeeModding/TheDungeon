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

public abstract class BasicReplacementProcessor extends StructureProcessor {
    protected abstract Map<Block, WeightedMap.Int<BlockState>> replacements();

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings) {
        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        WeightedMap.Int<BlockState> options = this.replacements().get(relativeBlockInfo.state().getBlock());
        if (options == null)
            return relativeBlockInfo;
        BlockState newBlockstate = options.getRandom(randomsource);
        if (newBlockstate == null) {
            return relativeBlockInfo;
        } else {
            BlockState oldBlockstate = relativeBlockInfo.state();
            if (oldBlockstate.hasProperty(StairBlock.FACING) &&
                    newBlockstate.hasProperty(StairBlock.FACING)) {
                newBlockstate = newBlockstate.setValue(StairBlock.FACING, oldBlockstate.getValue(StairBlock.FACING));
            }

            if (oldBlockstate.hasProperty(StairBlock.HALF) &&
                    newBlockstate.hasProperty(StairBlock.HALF)) {
                newBlockstate = newBlockstate.setValue(StairBlock.HALF, oldBlockstate.getValue(StairBlock.HALF));
            }

            if (oldBlockstate.hasProperty(SlabBlock.TYPE) &&
                    newBlockstate.hasProperty(SlabBlock.TYPE)) {
                newBlockstate = newBlockstate.setValue(SlabBlock.TYPE, oldBlockstate.getValue(SlabBlock.TYPE));
            }


            if (oldBlockstate.hasProperty(RotatedPillarBlock.AXIS) &&
                    newBlockstate.hasProperty(RotatedPillarBlock.AXIS)) {
                newBlockstate = newBlockstate.setValue(RotatedPillarBlock.AXIS, oldBlockstate.getValue(RotatedPillarBlock.AXIS));
            }

            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
        }
    }
}
