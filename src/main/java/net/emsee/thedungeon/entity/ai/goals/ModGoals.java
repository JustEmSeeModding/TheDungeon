package net.emsee.thedungeon.entity.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public final class ModGoals {
    public static class GhastLikeLookGoal extends Goal {
        private final FlyingMob entity;

        public GhastLikeLookGoal(FlyingMob entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.entity.getTarget() == null) {
                Vec3 vec3 = this.entity.getDeltaMovement();
                this.entity.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float)Math.PI));
                this.entity.yBodyRot = this.entity.getYRot();
            } else {
                LivingEntity livingentity = this.entity.getTarget();
                double d0 = 64.0;
                if (livingentity.distanceToSqr(this.entity) < 4096.0) {
                    double d1 = livingentity.getX() - this.entity.getX();
                    double d2 = livingentity.getZ() - this.entity.getZ();
                    this.entity.setYRot(-((float)Mth.atan2(d1, d2)) * (180.0F / (float)Math.PI));
                    this.entity.yBodyRot = this.entity.getYRot();
                }
            }
        }
    }

    public static class GhastLikeRandomFloatAroundGoal extends Goal {
        private final FlyingMob entity;

        public GhastLikeRandomFloatAroundGoal(FlyingMob entity) {
            this.entity = entity;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movecontrol = this.entity.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.entity.getX();
                double d1 = movecontrol.getWantedY() - this.entity.getY();
                double d2 = movecontrol.getWantedZ() - this.entity.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource randomsource = this.entity.getRandom();
            double d0 = this.entity.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.entity.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.entity.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.entity.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
        }
    }

    public static class FlyingMoveToTargetGoal extends Goal {
        private final FlyingMob entity;
        private final float boundingBoxTouch;

        public FlyingMoveToTargetGoal(FlyingMob entity, float boundingBoxInflation) {
            this.entity = entity;
            this.boundingBoxTouch=boundingBoxInflation;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            return entity.getTarget() != null && !entity.getBoundingBox().inflate(boundingBoxTouch).intersects(entity.getTarget().getBoundingBox());
        }

        @Override
        public boolean canContinueToUse() {
            return entity.getTarget() != null && !entity.getBoundingBox().inflate(boundingBoxTouch).intersects(entity.getTarget().getBoundingBox());
        }

        @Override
        public void tick() {
            LivingEntity target = entity.getTarget();
            if (target!=null) {
                this.entity.getMoveControl().setWantedPosition(target.position().x, target.getEyePosition().y, target.position().z, 1.0);
            }
        }
    }
}
