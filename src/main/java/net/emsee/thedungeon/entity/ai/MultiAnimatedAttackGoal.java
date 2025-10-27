package net.emsee.thedungeon.entity.ai;

import com.mojang.datafixers.util.Pair;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.entity.custom.interfaces.IBasicAnimatedEntity;
import net.emsee.thedungeon.entity.custom.interfaces.IMultiAttackAnimatedEntity;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/** attack goal with multiple different animated attacks */
public class MultiAnimatedAttackGoal<T extends DungeonPathfinderMob & IBasicAnimatedEntity & IMultiAttackAnimatedEntity> extends MeleeAttackGoal {
    private final T entity;
    private int ticksUntilNextAttack;
    private boolean attacked = false;
    private boolean shouldCountTillNextAttack = false;
    private final WeightedMap.Int<AttackHolder> attackHolders = new WeightedMap.Int<>();
    private AttackHolder currentAttackHolder;
    private byte AnimationVersion;

    public MultiAnimatedAttackGoal(T pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    public MultiAnimatedAttackGoal<T> withAttack(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer) {
        return withAttack(animationID, attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, consumer, 1);
    }

    public MultiAnimatedAttackGoal<T> withAttack(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer, int weight) {
        return withAttack(new AttackHolder(animationID, attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, consumer),weight);
    }

    public MultiAnimatedAttackGoal<T> withAttack(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier) {
        return withAttack(animationID, attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, null, 1);
    }

    public MultiAnimatedAttackGoal<T> withAttack(int animationID,int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, int weight) {
        return withAttack(animationID, attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, null, weight);
    }

    public MultiAnimatedAttackGoal<T> withAttack(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer, Pair<List<Item>,List<Item>> requiredItems, int weight) {
        return withAttack(new AttackHolder(animationID, attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, consumer, requiredItems),weight);
    }

    private MultiAnimatedAttackGoal<T> withAttack(AttackHolder holder, int weight) {
        attackHolders.put(holder,weight);
        return this;
    }

    @Override
    public boolean canUse() {
        //if (entity.isRunning()) return false;
        if (attackHolders.isEmpty()) return false;
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        //if (entity.isRunning()) return false;
        if (attackHolders.isEmpty()) return false;
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        currentAttackHolder = getPossibleAttacks(entity).getRandom(entity.level().getRandom());
        resetAttackCooldown();
        AnimationVersion=1;
        entity.attackAnimation((byte) -1, AnimationVersion++);
    }


    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy) {
        if (canPerformAttack(pEnemy) || entity.isPlayingAttackAnimation()) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.attackAnimation(currentAttackHolder.animationID, AnimationVersion);
                entity.setAttacking(true);
            }

            if(isTimeToAttack()) {
                performAttack(pEnemy);
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
            }

            if(animationFinished()){
                currentAttackHolder = getPossibleAttacks(entity).getRandom(entity.level().getRandom());
                this.resetAttackCooldown();
                AnimationVersion++;
                if (AnimationVersion == 0)
                    AnimationVersion++;
            }
        } else {
            currentAttackHolder = getPossibleAttacks(entity).getRandom(entity.level().getRandom());
            this.resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            AnimationVersion=0;
            entity.attackAnimation((byte) -1, AnimationVersion);
        }
    }

    WeightedMap.Int<AttackHolder> getPossibleAttacks(LivingEntity entity) {
        WeightedMap.Int<AttackHolder> toReturn = new WeightedMap.Int<>();
        for (Map.Entry<AttackHolder,Integer> entry : attackHolders.entrySet()) {
            AttackHolder holder = entry.getKey();
            if ((holder.requiredItems.getFirst().isEmpty() || holder.requiredItems.getFirst().contains(entity.getMainHandItem().getItem())) &&
                    (holder.requiredItems.getSecond().isEmpty() || holder.requiredItems.getSecond().contains(entity.getOffhandItem().getItem())))
                toReturn.put(entry.getKey(), entry.getValue());
        }

        return toReturn;
    }

    private boolean animationFinished() {
        return ticksUntilNextAttack<=0;
    }

    @Override
    protected boolean canPerformAttack(@NotNull LivingEntity pEnemy) {
        return entity.isWithinMeleeAttackRange(pEnemy, currentAttackHolder.reachMultiplier) && entity.getSensing().hasLineOfSight(pEnemy);
    }

    @Override
    protected void resetAttackCooldown() {
        attacked = false;
        this.ticksUntilNextAttack = this.adjustedTickDelay(currentAttackHolder.attackDelay + currentAttackHolder.attackCooldown);
    }

    @Override
    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= currentAttackHolder.attackCooldown && !attacked;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= currentAttackHolder.attackDelay+ currentAttackHolder.attackCooldown;
    }

    @Override
    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected void performAttack(LivingEntity pEnemy) {
        switch(currentAttackHolder.hand) {
            case RIGHT -> entity.swing(InteractionHand.MAIN_HAND);
            case LEFT -> entity.swing(InteractionHand.OFF_HAND);
            case BOTH -> {
                entity.swing(InteractionHand.MAIN_HAND);
                entity.swing(InteractionHand.OFF_HAND);
            }
        }

        if (canPerformAttack(pEnemy)) {
            entity.doHurtTargetMultiplier(pEnemy, currentAttackHolder.damageMultiplier, currentAttackHolder.knockbackMultiplier);
        }
        if (currentAttackHolder.consumer!= null)
            currentAttackHolder.consumer.accept(entity, pEnemy);
        attacked = true;
    }

    @Override
    public void tick() {
        super.tick();
        if(shouldCountTillNextAttack) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    public void stop() {
        entity.setAttacking(false);
        AnimationVersion = 0;
        entity.attackAnimation((byte) -1, AnimationVersion);
        super.stop();
    }

    public enum AttackHand{
        RIGHT,
        LEFT,
        BOTH,
        NONE
    }

    public class AttackHolder {
        protected final int animationID;
        protected final int attackDelay;
        protected final int attackCooldown;
        protected final float damageMultiplier;
        protected final float knockbackMultiplier;
        protected final float reachMultiplier;
        protected final BiConsumer<T,LivingEntity> consumer;
        protected final AttackHand hand;
        protected final Pair<List<Item>, List<Item>> requiredItems;

        public AttackHolder(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer) {
            this.animationID =animationID;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
            this.damageMultiplier = damageMultiplier;
            this.knockbackMultiplier = knockbackMultiplier;
            this.reachMultiplier = reachMultiplier;
            this.consumer = consumer;
            this.requiredItems = Pair.of(List.of(), List.of());
            this.hand = AttackHand.RIGHT;
        }

        AttackHolder(int animationID,int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer, AttackHand hand) {
            this.animationID = animationID;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
            this.damageMultiplier = damageMultiplier;
            this.knockbackMultiplier = knockbackMultiplier;
            this.reachMultiplier = reachMultiplier;
            this.consumer = consumer;
            this.requiredItems = Pair.of(List.of(), List.of());
            this.hand = hand;
        }

        AttackHolder(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer, Pair<List<Item>,List<Item>> requiredItems) {
            this.animationID =animationID;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
            this.damageMultiplier = damageMultiplier;
            this.knockbackMultiplier = knockbackMultiplier;
            this.reachMultiplier = reachMultiplier;
            this.consumer = consumer;
            this.requiredItems = requiredItems;
            this.hand = AttackHand.RIGHT;
        }

        AttackHolder(int animationID, int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, BiConsumer<T,LivingEntity> consumer, Pair<List<Item>,List<Item>> requiredItems, AttackHand hand) {
            this.animationID =animationID;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
            this.damageMultiplier = damageMultiplier;
            this.knockbackMultiplier = knockbackMultiplier;
            this.reachMultiplier = reachMultiplier;
            this.consumer = consumer;
            this.requiredItems = requiredItems;
            this.hand = hand;
        }
    }
}
