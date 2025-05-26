package net.emsee.thedungeon.entity.custom;

//TODO fix this

import net.emsee.thedungeon.entity.ai.goals.ModGoals;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FlyingBookEntity extends FlyingMob implements Enemy {

    public FlyingBookEntity(EntityType<? extends FlyingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ModGoals.FlyingMoveToTargetGoal(this, .15f));
        this.goalSelector.addGoal(2, new FlyingBookAttackGoal());
        this.goalSelector.addGoal(3, new ModGoals.GhastLikeRandomFloatAroundGoal(this));
        this.goalSelector.addGoal(4, new ModGoals.GhastLikeLookGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, true, p_327010_ -> Math.abs(p_327010_.getY() - this.getY()) <= 4.0));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return FlyingMob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0)
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 4.0)
                //.add(Attributes.MOVEMENT_SPEED, 4.0)
                //.add(Attributes.JUMP_STRENGTH, 1.0)
                .add(Attributes.ATTACK_DAMAGE,1.0)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                ;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }


    class FlyingBookAttackGoal extends Goal {
        private static final int ATTACK_DELAY = 20;
        private int nextAttackTick;

        @Override
        public boolean canUse() {
            return FlyingBookEntity.this.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity livingentity = FlyingBookEntity.this.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                if (livingentity instanceof Player player && (livingentity.isSpectator() || player.isCreative())) {
                    return false;
                }
                return this.canUse();
            }
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            FlyingBookEntity.this.setTarget(null);
        }

        @Override
        public void tick() {
            boolean attackThisTick = false;

            if (FlyingBookEntity.this.tickCount > this.nextAttackTick) {
                attackThisTick = true;
            }

            LivingEntity livingentity = FlyingBookEntity.this.getTarget();
            if (attackThisTick && livingentity != null) {
                if (FlyingBookEntity.this.getBoundingBox().inflate(0.2F).intersects(livingentity.getBoundingBox())) {
                    FlyingBookEntity.this.doHurtTarget(livingentity);
                    this.nextAttackTick = FlyingBookEntity.this.tickCount + 20;
                    if (!FlyingBookEntity.this.isSilent()) {
                        //FlyingBookEntity.this.level().levelEvent(1039, FlyingBookEntity.this.blockPosition(), 0);
                    }
                }
            }
        }
    }
}
