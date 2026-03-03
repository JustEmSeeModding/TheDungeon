package net.emsee.thedungeon.entity.brain;

import net.emsee.thedungeon.entity.attack.AbstractAttackPattern;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

/** Central brain to manage attacks and AI decisions */
public class DungeonMobBrain<E extends DungeonAnimatedMob> {
    private final E entity;
    private final WeightedMap.Int<AbstractAttackPattern<E>> attacks = new WeightedMap.Int<>();
    private AbstractAttackPattern<E> currentAttack = null;
    private final Random random = new Random();
    private byte lastNetworkAnimationID = -1;

    public DungeonMobBrain(E entity) {
        this.entity = entity;
    }

    public void addAttack(int weight, AbstractAttackPattern<E> attack) {
        attacks.put(attack, weight);
    }

    public DungeonMobBrain<E> withAttack(int weight, AbstractAttackPattern<E> attack) {
        addAttack(weight, attack);
        return this;
    }

    public void addAttack(AbstractAttackPattern<E> attack) {
        addAttack(1, attack);
    }

    public DungeonMobBrain<E> withAttack(AbstractAttackPattern<E> attack) {
        return withAttack(1, attack);
    }

    /**
     * Tick the brain every game tick
     */
    public void tick(E entity) {
        LivingEntity target = entity.getTarget();
        if (target != null) {
            if (currentAttack != null) {
                currentAttack.tick(entity, target);
                if (currentAttack.isFinished(entity)) {
                    currentAttack = null;
                }
            } else {
                // Pick a new attack if possible
                currentAttack = selectAttack(target);
                if (currentAttack != null) {
                    currentAttack.start(entity, target);
                    entity.startAttackAnimation(currentAttack.getAnimationID(), ++lastNetworkAnimationID);
                }
            }
        } else {
            currentAttack = null;
        }

        for (AbstractAttackPattern<E> p : attacks.keySet()) {
            p.passiveTick(entity);
        }
        // Tick current attack if active

    }

    private AbstractAttackPattern<E> selectAttack(LivingEntity target) {
        WeightedMap.Int<AbstractAttackPattern<E>> usable = new WeightedMap.Int<>();
        for (AbstractAttackPattern<E> attack : attacks.keySet()) {
            if (testAttackCanUse(attack, target)) {
                usable.put(attack, attacks.getWeight(attack));
            }
        }
        if (usable.isEmpty()) return null;
        return usable.getRandom(random);
    }

    private boolean testAttackCanUse(AbstractAttackPattern<E> attackPattern, LivingEntity target) {
        return attackPattern.canUseNow() &&
                attackPattern.canUseAgainstNow(entity, target) &&
                attackPattern.canUseWithItems(entity.getMainHandItem(), entity.getOffhandItem());
    }

    public boolean isAttacking() {
        return currentAttack != null;
    }

    public AbstractAttackPattern<E> getCurrentAttack() {
        return currentAttack;
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
