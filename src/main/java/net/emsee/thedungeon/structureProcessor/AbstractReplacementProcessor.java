package net.emsee.thedungeon.structureProcessor;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public abstract class AbstractReplacementProcessor extends StructureProcessor {
    protected BlockState copyAllProperties(BlockState from, BlockState to) {
        to = copyProperty(from, to, StairBlock.FACING);
        to = copyProperty(from, to, StairBlock.HALF);
        to = copyProperty(from, to, SlabBlock.TYPE);
        to = copyProperty(from, to, RotatedPillarBlock.AXIS);
        to = copyProperty(from, to, HorizontalDirectionalBlock.FACING);
        return to;
    }

    protected <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        if (from.hasProperty(property)) {
            return to.trySetValue(property, from.getValue(property));
        }
        return to;
    }
}
