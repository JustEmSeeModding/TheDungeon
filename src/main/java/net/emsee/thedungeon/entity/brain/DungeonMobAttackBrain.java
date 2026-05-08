package net.emsee.thedungeon.entity.brain;

import net.emsee.thedungeon.entity.attack.AbstractAttackPattern;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

/** Central brain to manage attacks and AI decisions */
public class DungeonMobAttackBrain<E extends DungeonAnimatedMob> {
    protected final E entity;
    protected final WeightedMap.Int<AbstractAttackPattern<E>> attacks = new WeightedMap.Int<>();
    protected AbstractAttackPattern<E> currentAttack = null;
    protected final Random random = new Random();
    protected byte lastNetworkAnimationID = -1;
    protected int globalCooldown;
    protected int globalAttackStartCooldownAmount;
    protected int globalAttackEndCooldownAmount;

    public DungeonMobAttackBrain(E entity) {
        this.entity = entity;
    }

    public void addAttack(int weight, AbstractAttackPattern<E> attack) {
        attacks.put(attack, weight);
    }

    public DungeonMobAttackBrain<E> withAttack(int weight, AbstractAttackPattern<E> attack) {
        addAttack(weight, attack);
        return this;
    }

    public void addAttack(AbstractAttackPattern<E> attack) {
        addAttack(1, attack);
    }

    public DungeonMobAttackBrain<E> withAttack(AbstractAttackPattern<E> attack) {
        return withAttack(1, attack);
    }

    /**
     * attack start cooldown starts counting from the moment the attack is triggered,
     * these cooldowns are separate from any cooldown the attack might have
     */
    public void useGlobalAttackStartCooldown(int amount) {
        if  (amount <= 0) throw new IllegalArgumentException("cooldown can't be less than or equal to 0");
        if  (this.globalAttackStartCooldownAmount > 0) throw new IllegalStateException("cooldown is already set");
        this.globalAttackStartCooldownAmount = amount;
    }
    /**
     * attack start cooldown starts counting from the moment the attack is triggered,
     * these cooldowns are separate from any cooldown the attack might have
     */
    public DungeonMobAttackBrain<E> withGlobalAttackStartCooldown(int amount) {
        useGlobalAttackStartCooldown(amount);
        return this;
    }

    /**
     * attack end cooldown starts counting from the moment the attack is finished,
     * these cooldowns are separate from any cooldown the attack might have
     */
    public void useGlobalAttackEndCooldown(int amount) {
        if  (amount <= 0) throw new IllegalArgumentException("cooldown can't be less than or equal to 0");
        if  (this.globalAttackEndCooldownAmount > 0) throw new IllegalStateException("cooldown is already set");
        this.globalAttackEndCooldownAmount = amount;
    }
    /**
     * attack end cooldown starts counting from the moment the attack is finished,
     * these cooldowns are separate from any cooldown the attack might have
     */
    public DungeonMobAttackBrain<E> withGlobalAttackEndCooldown(int amount) {
        useGlobalAttackEndCooldown(amount);
        return this;
    }

    /**
     * Tick the brain every game tick
     */
    public void tick() {
        LivingEntity target = entity.getTarget();
        targetingTick(target);
        if (globalCooldown > 0) {
            globalCooldown--;
        }
        for (AbstractAttackPattern<E> p : attacks.keySet()) {
            p.passiveTick();
        }
        // Tick current attack if active
    }

    protected void targetingTick(LivingEntity target) {
        if (target != null) {
            if (currentAttack != null) {
                currentAttack.tick(target);
                if (currentAttack.isFinished()) {
                    triggerCooldown(globalAttackEndCooldownAmount);
                    currentAttack = null;
                }
            } else if (canAttackNow(entity, target)) {
                // Pick a new attack if possible
                currentAttack = selectAttack(target);
                if (currentAttack != null) {
                    attackStart(currentAttack, target);
                }
            }
        } else {
            currentAttack = null;
        }
    }

    private void attackStart(AbstractAttackPattern<E> attack, LivingEntity target) {
        attack.start(target);
        entity.startAttackAnimation(attack.getAnimationID(), ++lastNetworkAnimationID);
        triggerCooldown(globalAttackStartCooldownAmount);
    }

    protected AbstractAttackPattern<E> selectAttack(LivingEntity target) {
        WeightedMap.Int<AbstractAttackPattern<E>> usable = new WeightedMap.Int<>();
        for (AbstractAttackPattern<E> attack : attacks.keySet()) {
            if (testAttackPatternCanUse(attack, target)) {
                usable.put(attack, attacks.getWeight(attack));
            }
        }
        if (usable.isEmpty()) return null;
        return usable.getRandom(random);
    }

    protected void triggerCooldown(int amount) {
        globalCooldown = Math.max(globalCooldown, amount);
    }

    protected boolean canAttackNow(E entity, LivingEntity target) {
        return globalCooldown <= 0 && !attacks.isEmpty();
    }

    protected boolean testAttackPatternCanUse(AbstractAttackPattern<E> attackPattern, LivingEntity target) {
        return attackPattern.canUseNow(entity) &&
                attackPattern.canUseAgainstNow(entity, target) &&
                attackPattern.canUseWithItems(entity.getMainHandItem(), entity.getOffhandItem());
    }

    public boolean isAttacking() {
        return currentAttack != null;
    }

    public AbstractAttackPattern<E> getCurrentAttack() {
        return currentAttack;
    }

    public int getRemainingCooldown() {
        return globalCooldown;
    }

    public CompoundTag toSaveTag() {
        CompoundTag tag = new CompoundTag();
        tag.putByte("lastAnimId", lastNetworkAnimationID);
        final int[] i = {0};
        attacks.forEach((pattern, weight) -> {
            tag.put("Attack" + i[0] + "Data", pattern.toSaveTag());
            i[0]++;
        });
        return tag;
    }

    public void fromSaveTag(CompoundTag tag) {
        lastNetworkAnimationID = tag.getByte("lastAnimId");
        final int[] i = {0};
        attacks.forEach((pattern, weight) -> {
            pattern.fromSaveTag(tag.getCompound("Attack" + i[0] + "Data"));
            i[0]++;
        });
    }
}
