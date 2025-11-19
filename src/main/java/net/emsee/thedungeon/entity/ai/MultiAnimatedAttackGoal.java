package net.emsee.thedungeon.entity.ai;

import com.mojang.datafixers.util.Pair;
import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.entity.custom.interfaces.IBasicAnimatedEntity;
import net.emsee.thedungeon.entity.custom.interfaces.IMultiAttackAnimatedEntity;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public MultiAnimatedAttackGoal<T> withAttack(int animationID,int attackDelay, int attackCooldown, Function<AttackHolder, AttackHolder> holderFunction, int weight) {
        return withAttack(holderFunction.apply(new AttackHolder(animationID, attackDelay, attackCooldown)),weight);
    }

    public MultiAnimatedAttackGoal<T> withAttack(AttackHolder holder, int weight) {
        attackHolders.put(holder,weight);
        return this;
    }

    @Override
    public boolean canUse() {
        return !getPossibleAttacks(this.mob.getTarget()).isEmpty() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        //if (entity.isRunning()) return false;
        LivingEntity enemy = this.mob.getTarget();
        if (attackHolders.isEmpty()) return false;
        //if (!canPerformAttack(enemy) && !entity.isPlayingAttackAnimation() && !entity.isAttacking()) return false;
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
        //currentAttackHolder = getPossibleAttacks(entity).getRandom(entity.level().getRandom());
        //resetAttackCooldown();
        currentAttackHolder = getPossibleAttacks(entity.getTarget()).getRandom(entity.level().getRandom());
        if (currentAttackHolder == null) throw new IllegalStateException("no valid holder found");
//        {
//            // No valid attack with current equipment: disable until next tick
//            shouldCountTillNextAttack = false;
//            entity.setAttacking(false);
//            AnimationVersion = 0;
//            entity.attackAnimation((byte) -1, AnimationVersion);
//            return;
//        }
        resetAttackCooldown();
        AnimationVersion = 1;
        entity.attackAnimation((byte) -1, AnimationVersion++);
    }


    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy) {
        if (currentAttackHolder == null) {
            currentAttackHolder = getPossibleAttacks(pEnemy).getRandom(entity.level().getRandom());
            shouldCountTillNextAttack = false;
            entity.setAttacking(false);
            AnimationVersion = 0;
            entity.attackAnimation(-1, AnimationVersion);
            if (currentAttackHolder == null) return;
            this.resetAttackCooldown();
        }
        if (canPerformAttack(pEnemy) || entity.isPlayingAttackAnimation() || entity.isAttacking()) {
            shouldCountTillNextAttack = true;
            entity.setAttacking(true);

            if(isTimeToStartAttackAnimation()) {
                entity.attackAnimation(currentAttackHolder.animationID, AnimationVersion);
            }

            if(isTimeToAttack()) {
                performAttack(pEnemy);
                this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
            }

            if(animationFinished()) {
                currentAttackHolder = getPossibleAttacks(pEnemy).getRandom(entity.level().getRandom());
                //this.resetAttackCooldown();
                if (currentAttackHolder == null) {
                    shouldCountTillNextAttack = false;
                    this.resetAttackCooldown();
                    entity.setAttacking(false);
                    AnimationVersion = 0;
                    entity.attackAnimation(-1, AnimationVersion);
                    return;
                }
                AnimationVersion++;
                if (AnimationVersion == 0)
                    AnimationVersion=1;
                this.resetAttackCooldown();
            }
        }
    }

    WeightedMap.Int<AttackHolder> getPossibleAttacks(LivingEntity pEnemy) {
        WeightedMap.Int<AttackHolder> toReturn = new WeightedMap.Int<>();
        if (pEnemy == null) return toReturn;
        for (Map.Entry<AttackHolder,Integer> entry : attackHolders.entrySet()) {
            AttackHolder holder = entry.getKey();

            double reach = switch (holder.reachMode) {
                case MULTIPLIER -> holder.reach * entity.getAttributeValue(ModAttributes.DUNGEON_MOB_REACH);
                case OVERRIDE -> holder.reach;
            };

            boolean canReach = entity.isWithinMeleeAttackRange(pEnemy, holder.minReach, reach);

            if ((holder.requiredItems.getFirst().isEmpty() || holder.requiredItems.getFirst().contains(entity.getMainHandItem().getItem())) &&
                    (holder.requiredItems.getSecond().isEmpty() || holder.requiredItems.getSecond().contains(entity.getOffhandItem().getItem())) &&
                    canReach)
                toReturn.put(entry.getKey(), entry.getValue());
        }

        return toReturn;
    }

    private boolean animationFinished() {
        return ticksUntilNextAttack<=0;
    }

    @Override
    protected boolean canPerformAttack(@NotNull LivingEntity pEnemy) {
        if (currentAttackHolder==null) return false;
        double reach = switch (currentAttackHolder.reachMode) {
            case MULTIPLIER ->
                 currentAttackHolder.reach * entity.getAttributeValue(ModAttributes.DUNGEON_MOB_REACH);
            case OVERRIDE ->
                 currentAttackHolder.reach;
        };


        boolean canReach = entity.isWithinMeleeAttackRange(pEnemy, currentAttackHolder.minReach, reach);


        return canReach && entity.getSensing().hasLineOfSight(pEnemy);
    }

    @Override
    protected void resetAttackCooldown() {
        attacked = false;
        if (currentAttackHolder==null) return;
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
        protected enum ReachMode{
            MULTIPLIER,
            OVERRIDE
        }


        protected final int animationID;
        protected final int attackDelay;
        protected final int attackCooldown;
        protected float damageMultiplier = 1;
        protected float knockbackMultiplier = 1;
        protected float minReach = 0;
        protected float reach = 1;
        protected ReachMode reachMode = ReachMode.MULTIPLIER;
        protected BiConsumer<T,LivingEntity> consumer;
        protected AttackHand hand = AttackHand.RIGHT;
        protected Pair<List<Item>, List<Item>> requiredItems = Pair.of(List.of(), List.of());

        public AttackHolder(int animationID, int attackDelay, int attackCooldown) {
            this.animationID =animationID;
            this.attackDelay = attackDelay;
            this.attackCooldown = attackCooldown;
        }

        public AttackHolder withDamageMultiplier(float multiplier) {
            if (multiplier<0) throw new IllegalArgumentException("Damage Multiplier Can't Be Negative");
            damageMultiplier = multiplier;
            reachMode = ReachMode.MULTIPLIER;
            return this;
        }

        public AttackHolder withKnockbackMultiplier(float multiplier) {
            if (multiplier<0) throw new IllegalArgumentException("Knockback Multiplier Can't Be Negative");
            knockbackMultiplier = multiplier;
            reachMode = ReachMode.OVERRIDE;
            return this;
        }

        public AttackHolder withReachMultiplier(float multiplier) {
            if (multiplier<0) throw new IllegalArgumentException("Reach multiplier Can't Be Negative");
            reach = multiplier;
            return this;
        }

        public AttackHolder withReachOverride(float override) {
            if (override<0) throw new IllegalArgumentException("Reach Can't Be Negative");
            reach = override;
            return this;
        }

        public AttackHolder withMinReach(float reach) {
            if (reach<0) throw new IllegalArgumentException("Minimum Reach Can't Be Negative");
            minReach = reach;
            return this;
        }

        public AttackHolder withAttackConsumer(BiConsumer<T,LivingEntity> consumer) {
            this.consumer = consumer;
            return this;
        }

        public AttackHolder withAttackHand(AttackHand hand) {
            this.hand = hand;
            return this;
        }

        public AttackHolder withRequiredItems(List<Item> mainHandOptions, List<Item> offHandOptions) {
            requiredItems = Pair.of(new ArrayList<>(mainHandOptions), new ArrayList<>(offHandOptions));
            return this;
        }
    }
}
