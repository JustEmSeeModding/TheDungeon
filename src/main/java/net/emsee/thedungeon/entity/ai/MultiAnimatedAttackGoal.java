package net.emsee.thedungeon.entity.ai;

import net.emsee.thedungeon.dungeon.room.GridRoom;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.entity.custom.interfaces.IBasicAnimatedEntity;
import net.emsee.thedungeon.entity.custom.interfaces.IMultiAttackAnimatedEntity;
import net.emsee.thedungeon.utils.ListAndArrayUtils;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/** attack goal with multiple different animated attacks */
public class MultiAnimatedAttackGoal<T extends DungeonPathfinderMob & IBasicAnimatedEntity & IMultiAttackAnimatedEntity> extends MeleeAttackGoal {
    private byte lastId = 0;
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

    public MultiAnimatedAttackGoal<T> withAttack(int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, Consumer<T> consumer) {
        withAttack(attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, consumer, 1);
        return this;
    }

    public MultiAnimatedAttackGoal<T> withAttack(int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, Consumer<T> consumer, int weight) {
        attackHolders.put(new AttackHolder(attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, consumer),weight);
        return this;
    }

    public MultiAnimatedAttackGoal<T> withAttack(int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier) {
        withAttack(attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, null, 1);
        return this;
    }

    public MultiAnimatedAttackGoal<T> withAttack(int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, int weight) {
        this.withAttack(attackDelay, attackCooldown, damageMultiplier, knockbackMultiplier, reachMultiplier, null, weight);
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
        currentAttackHolder = attackHolders.getRandom(entity.level().getRandom());
        resetAttackCooldown();
        AnimationVersion=0;
        entity.attackAnimation((byte) -1, AnimationVersion++);
    }


    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy) {
        if (canPerformAttack(pEnemy) || entity.isPlayingAttackAnimation()) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.attackAnimation(currentAttackHolder.id, AnimationVersion);
                entity.setAttacking(true);
            }

            if(isTimeToAttack()) {
                performAttack(pEnemy);
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
            }

            if(animationFinished()){
                currentAttackHolder = attackHolders.getRandom(entity.level().getRandom());
                this.resetAttackCooldown();
                AnimationVersion++;
                if (AnimationVersion == 0)
                    AnimationVersion++;
            }
        } else {
            currentAttackHolder = attackHolders.getRandom(entity.level().getRandom());
            this.resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            AnimationVersion=0;
            entity.attackAnimation((byte) -1, AnimationVersion);
        }
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
            currentAttackHolder.consumer.accept(entity);
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

    private class AttackHolder {
        protected final byte id;
        protected final int attackDelay;
        protected final int attackCooldown;
        protected final float damageMultiplier;
        protected final float knockbackMultiplier;
        protected final float reachMultiplier;
        protected final Consumer<T> consumer;
        protected final AttackHand hand;

        AttackHolder(int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, Consumer<T> consumer) {
            id=lastId++;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
            this.damageMultiplier = damageMultiplier;
            this.knockbackMultiplier = knockbackMultiplier;
            this.reachMultiplier = reachMultiplier;
            this.consumer = consumer;
            this.hand = AttackHand.RIGHT;
        }

        AttackHolder(int attackDelay, int attackCooldown, float damageMultiplier, float knockbackMultiplier, float reachMultiplier, Consumer<T> consumer, AttackHand hand) {
            id=lastId++;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
            this.damageMultiplier = damageMultiplier;
            this.knockbackMultiplier = knockbackMultiplier;
            this.reachMultiplier = reachMultiplier;
            this.consumer = consumer;
            this.hand = hand;
        }
    }
}
