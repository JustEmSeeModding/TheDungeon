package net.emsee.thedungeon.structureProcessor;

import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractReplacementProcessor extends StructureProcessor {
    protected AbstractReplacementProcessor() {
        super();
    }

    protected abstract Map<Block, WeightedMap.Int<ReplaceInstance>> getReplacements();


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

    protected static class ReplaceInstance {
        public final Supplier<BlockState> stateSupplier;
        Predicate<PredicateInfo> predicate = predicateInfo -> true;
        final int separationID;

        public ReplaceInstance(Supplier<BlockState> stateSupplier) {
            this.stateSupplier = stateSupplier;
            separationID = 0;
        }

        public ReplaceInstance(Supplier<BlockState> stateSupplier, int separationID) {
            this.stateSupplier = stateSupplier;
            this.separationID = separationID;
        }

        public ReplaceInstance withPredicate(Predicate<PredicateInfo> newPredicate) {
            predicate = predicate.and(newPredicate);
            return this;
        }

        public boolean test(LevelReader level, BlockPos offset, BlockPos pos,
                            StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
            PredicateInfo pInfo = new PredicateInfo(level, offset, pos, blockInfo, relativeBlockInfo, settings, template);
            return predicate.test(pInfo);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReplaceInstance that = (ReplaceInstance) o;
            return separationID == that.separationID && Objects.equals(stateSupplier, that.stateSupplier) && Objects.equals(predicate, that.predicate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(stateSupplier, predicate, separationID);
        }
    }

    protected static class PredicateInfo {
        public final LevelReader level;
        public final BlockPos offset;
        public final BlockPos pos;
        public final StructureTemplate.StructureBlockInfo blockInfo;
        public final StructureTemplate.StructureBlockInfo relativeBlockInfo;
        public final StructurePlaceSettings settings;
        public final @Nullable StructureTemplate template;

        public PredicateInfo(LevelReader level, BlockPos offset, BlockPos pos,
                             StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
            this.level = level;
            this.offset = offset;
            this.pos = pos;
            this.blockInfo = blockInfo;
            this.relativeBlockInfo = relativeBlockInfo;
            this.settings = settings;
            this.template = template;
        }
    }
}
