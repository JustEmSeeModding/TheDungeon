package net.emsee.thedungeon.entity.ai;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.entity.custom.interfaces.IBasicAnimatedEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class BasicAnimatedAttackGoal<T extends DungeonPathfinderMob & IBasicAnimatedEntity> extends MeleeAttackGoal {
    private final T entity;
    private final int attackDelay; // time in anim before hit
    private final int attackCooldown; // time in anim after hit
    private int ticksUntilNextAttack;
    private boolean shouldCountTillNextAttack = false;

    public BasicAnimatedAttackGoal(T pMob, int ticksBeforeHit, int ticksAfterHit, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
        attackDelay=ticksBeforeHit;
        attackCooldown=ticksAfterHit;
    }

    @Override
    public boolean canUse() {
        //if (entity.isRunning()) return false;
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        //if (entity.isRunning()) return false;
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        ticksUntilNextAttack = attackDelay;
    }


    @Override
    protected void checkAndPerformAttack(LivingEntity pEnemy) {
        if (canPerformAttack(pEnemy) || entity.isPlayingAttackAnimation()) {
            shouldCountTillNextAttack = true;

            if(isTimeToStartAttackAnimation()) {
                entity.setAttacking(true);
            }

            if(isTimeToAttack()) {
                performAttack(pEnemy);
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
            }
        } else {
            this.resetAttackCooldown();
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
        }
    }

    @Override
    protected boolean canPerformAttack(LivingEntity pEnemy) {
        return this.mob.isWithinMeleeAttackRange(pEnemy) && this.mob.getSensing().hasLineOfSight(pEnemy);
    }

    @Override
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay + attackCooldown);
    }

    @Override
    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }

    protected boolean isTimeToStartAttackAnimation() {
        return this.ticksUntilNextAttack <= attackDelay;
    }

    @Override
    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }

    protected void performAttack(LivingEntity pEnemy) {
        this.mob.swing(InteractionHand.MAIN_HAND);
        if (canPerformAttack(pEnemy)) {
            this.mob.doHurtTarget(pEnemy);
        }
        this.resetAttackCooldown();
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
        super.stop();
    }

}
