package net.emsee.thedungeon.structureProcessor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Predicates extends AbstractReplacementProcessor {
    public static class BaseBlockPredicate implements Predicate<PredicateInfo> {
        private final Direction direction;
        private final Supplier<Block> baseBlock;

        public BaseBlockPredicate(Direction direction, Block baseBlock) {
            this.direction=direction;
            this.baseBlock = () -> baseBlock;
        }

        public BaseBlockPredicate(Direction direction, DeferredBlock<?> baseBlock) {
            this.direction=direction;
            this.baseBlock = baseBlock::get;
        }

        @Override
        public boolean test(PredicateInfo predicateInfo) {
            BlockState worldBaseBlock = predicateInfo.level().getBlockState(predicateInfo.pos().relative(direction));
            return worldBaseBlock.is(baseBlock.get());
        }
    }

    public static class BaseSolidBlockPredicate implements Predicate<PredicateInfo> {
        private final Direction direction;

        public BaseSolidBlockPredicate(Direction direction) {
            this.direction=direction;
        }

        @Override
        public boolean test(PredicateInfo predicateInfo) {
            BlockPos baseBlockPos = predicateInfo.pos().relative(direction);
            BlockState worldBaseBlock = predicateInfo.level().getBlockState(baseBlockPos);
            return worldBaseBlock.isFaceSturdy(predicateInfo.level(), baseBlockPos, direction.getOpposite());}
    }
}

