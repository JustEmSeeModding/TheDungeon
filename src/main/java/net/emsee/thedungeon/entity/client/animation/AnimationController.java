package net.emsee.thedungeon.entity.client.animation;

import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.minecraft.world.entity.AnimationState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AnimationController {
    private int lastAnimationId = -1;
    private byte lastVersion = -1;
    private int idleAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeoutAmount;

    private boolean cancelIdleAnimationWhenAttacking;
    public final Map<Integer, AttackAnimation> attackAnimationStates = new HashMap<>();

    public AnimationController() {}

    public AnimationController withIdleAnimation(int animationLength) {
        idleAnimationTimeoutAmount = animationLength;
        return this;
    }

    public AnimationController withAttackAnimation(int ID, int animationLength) {
        attackAnimationStates.put(ID, new AttackAnimation(new AnimationState(), animationLength));
        return this;
    }

    public AnimationController doCancelIdleAnimationWhenAttacking(boolean value) {
        cancelIdleAnimationWhenAttacking = value;
        return this;
    }

    public void tick(DungeonAnimatedMob entity) {
        tickIdleAnimation(entity);
        tickAttackAnimation(entity);
    }

    private void tickIdleAnimation(DungeonAnimatedMob entity) {
        boolean attackCancel = cancelIdleAnimationWhenAttacking && attackAnimationTimeout > 0;

        if (idleAnimationTimeout <= 0 && !attackCancel) {
            idleAnimationTimeout = idleAnimationTimeoutAmount-1; //first tick happens already, so we subtract 1 from the timeout
            idleAnimationState.start(entity.tickCount);
        } else if (attackCancel) {
            idleAnimationTimeout = 0;
            idleAnimationState.stop();
        }
        else {
            --idleAnimationTimeout;
        }
    }

    private void tickAttackAnimation(DungeonAnimatedMob entity) {
        int id = entity.getAttackAnimationId();
        byte version = entity.getAttackAnimationVersion();

        if (id != lastAnimationId || version != lastVersion) {
            AttackAnimation animation = attackAnimationStates.get(id);
            if (animation != null) {
                animation.state.start(entity.tickCount);
                attackAnimationTimeout = animation.timeout-1; //first tick happens already, so we subtract 1 from the timeout
            }
            lastVersion = version;
            lastAnimationId = id;
        } else {
            --attackAnimationTimeout;
        }
    }

    public boolean isPlayingAttack() {
        return attackAnimationTimeout > 0;
    }

    public record AttackAnimation(AnimationState state, int timeout) {}
}
