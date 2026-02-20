package net.emsee.thedungeon.entity.attack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;

public abstract class AttackPattern<E extends PathfinderMob> {

    /**
     * Called when the attack starts
     */
    public abstract void start(E entity, LivingEntity target);

    /**
     * Called every tick while the attack is in progress
     */
    public abstract void tick(E entity, LivingEntity target);

    /**
     * Called every tick even if this is not the used attack (for cooldowns etc...)
     */
    public abstract void passiveTick(E entity);

    /**
     * Whether the attack finished
     */
    public abstract boolean isFinished(E entity);

    /**
     * Damage multiplier
     */
    public abstract float getDamageMultiplier();

    /**
     * Knockback multiplier
     */
    public abstract float getKnockbackMultiplier();

    /**
     * Preferred hands used for this attack
     */
    public abstract AttackHand getHands();

    /**
     * Check if the entity has required items to perform this attack
     */
    public boolean canUseWithItems(ItemStack mainHand, ItemStack offHand) {
        return true;
    }

    /**
     * Check if the entity has any other requirements perform this attack
     */
    public boolean canUseAgainstNow(E entity, LivingEntity target) {
        return true;
    }

    public boolean canUseNow() {
        return true;
    }

    /**
     * Animation ID and duration
     */
    public abstract int getAnimationID();

    public abstract int getAnimationDuration();

    public abstract CompoundTag toSaveTag();

    public abstract void fromSaveTag(CompoundTag tag);

    public enum AttackHand {
        MAIN, OFF, BOTH
    }

    public static abstract class HandPredicate {
        public abstract boolean test(ItemStack stack);

        public static class Empty extends HandPredicate {
            @Override
            public boolean test(ItemStack stack) {
                return stack.isEmpty();
            }
        }

        public static class AlwaysTrue extends HandPredicate {
            @Override
            public boolean test(ItemStack stack) {
                return true;
            }
        }

        public static class ItemExact extends HandPredicate {
            private final ItemLike predicate;

            public ItemExact(ItemLike item) {
                predicate = item;
            }

            @Override
            public boolean test(ItemStack stack) {
                return stack.is(predicate.asItem());
            }
        }

        public static class ItemList extends HandPredicate {
            private final List<ItemLike> predicates;

            public ItemList(List<ItemLike> predicates) {
                this.predicates = predicates;
            }

            @Override
            public boolean test(ItemStack stack) {
                for (ItemLike predicate : predicates) {
                    if (stack.is(predicate.asItem()))
                        return true;
                }
                return false;
            }
        }

        public static class ItemListOrEmpty extends Or {
            public ItemListOrEmpty(List<ItemLike> predicates) {
                super(List.of(new Empty(), new ItemList(predicates)));
            }
        }

        public static class ItemExactOrEmpty extends Or {
            public ItemExactOrEmpty(ItemLike predicate) {
                super(List.of(new Empty(), new ItemExact(predicate)));
            }
        }

        public static class Or extends HandPredicate {
            private final List<HandPredicate> predicates;

            Or(List<HandPredicate> predicates) {
                this.predicates = predicates;
            }

            @Override
            public boolean test(ItemStack stack) {
                if (predicates.isEmpty()) return false;
                for (HandPredicate predicate : predicates) {
                    if (predicate.test(stack)) {
                        return true;
                    }
                }
                return false;
            }
        }

        public static class And extends HandPredicate {
            private final List<HandPredicate> predicates;

            And(List<HandPredicate> predicates) {
                this.predicates = predicates;
            }

            @Override
            public boolean test(ItemStack stack) {
                if (predicates.isEmpty()) return false;
                for (HandPredicate predicate : predicates) {
                    if (!predicate.test(stack)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
}
