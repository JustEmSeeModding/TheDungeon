package net.emsee.thedungeon.structureProcessor;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public abstract class Predicates extends AbstractReplacementProcessor {
    public static class BaseBlockPredicate implements Predicate<PredicateInfo> {
        final Direction direction;
        final Block baseBlock;

        public BaseBlockPredicate(Direction direction, Block baseBlock) {
            this.direction=direction;
            this.baseBlock = baseBlock;
        }

        @Override
        public boolean test(PredicateInfo predicateInfo) {
            BlockState worldBaseBlock = predicateInfo.level.getBlockState(predicateInfo.pos.relative(direction));
            return worldBaseBlock.is(baseBlock);
        }
    }

    public static class BaseSolidBlockPredicate implements Predicate<PredicateInfo> {
        final Direction direction;

        public BaseSolidBlockPredicate(Direction direction) {
            this.direction=direction;
        }

        @Override
        public boolean test(PredicateInfo predicateInfo) {
            BlockState worldBaseBlock = predicateInfo.level.getBlockState(predicateInfo.pos.relative(direction));
            return Block.isFaceFull(worldBaseBlock.getCollisionShape(predicateInfo.level, predicateInfo.pos.relative(direction)), direction.getOpposite());
        }
    }
}

