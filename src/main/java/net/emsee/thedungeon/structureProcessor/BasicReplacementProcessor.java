package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
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
            newBlockstate = copyAllProperties(oldBlockstate, newBlockstate);

            return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(), newBlockstate, relativeBlockInfo.nbt());
        }
    }

    protected BlockState copyAllProperties(BlockState from, BlockState to) {
        to=copyProperty(from,to,StairBlock.FACING);
        to=copyProperty(from,to,StairBlock.HALF);
        to=copyProperty(from,to,SlabBlock.TYPE);
        to=copyProperty(from,to,RotatedPillarBlock.AXIS);
        to=copyProperty(from,to,HorizontalDirectionalBlock.FACING);

        return to;
    }

    protected final <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        if (from.hasProperty(property)) {
            return to.trySetValue(property, from.getValue(property));
        }
        return to;
    }
}
