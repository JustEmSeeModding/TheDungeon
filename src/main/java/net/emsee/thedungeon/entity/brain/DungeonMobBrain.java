package net.emsee.thedungeon.entity.brain;

import net.emsee.thedungeon.entity.attack.AttackPattern;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Central brain to manage attacks and AI decisions */
public class DungeonMobBrain<E extends DungeonAnimatedMob> {
    private final E entity;
    private final List<AttackPattern<E>> attacks = new ArrayList<>();
    private AttackPattern<E> currentAttack = null;
    private final Random random = new Random();
    private byte lastNetworkAnimationID = -1;

    public DungeonMobBrain(E entity) {
        this.entity = entity;
    }

    public void addAttack(AttackPattern<E> attack) {
        attacks.add(attack);
    }

    /** Tick the brain every game tick */
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

        for (AttackPattern<E> p : attacks) {
            p.passiveTick(entity);
        }
        // Tick current attack if active

    }

    private AttackPattern<E> selectAttack(LivingEntity target) {
        List<AttackPattern<E>> usable = new ArrayList<>();
        for (AttackPattern<E> attack : attacks) {
            if (testAttackCanUse(attack, target)) {
                usable.add(attack);
            }
        }
        if (usable.isEmpty()) return null;
        return usable.get(random.nextInt(usable.size()));
    }

    private boolean testAttackCanUse(AttackPattern<E> attackPattern, LivingEntity target) {
        return attackPattern.canUseNow() &&
                attackPattern.canUseAgainstNow(entity, target) &&
                attackPattern.canUseWithItems(entity.getMainHandItem(), entity.getOffhandItem());
    }

    public boolean isAttacking() {
        return currentAttack != null;
    }

    public AttackPattern<E> getCurrentAttack() {
        return currentAttack;
    }

    public CompoundTag toSaveTag() {
        CompoundTag tag = new CompoundTag();
        tag.putByte("lastAnimId", lastNetworkAnimationID);
        int i = 0;
        for (AttackPattern<E> pattern : attacks) {
            tag.put("Attack"+i, pattern.toSaveTag());
            i++;
        }
        return tag;
    }

    public void fromSaveTag(CompoundTag tag) {
        lastNetworkAnimationID = tag.getByte("lastAnimId");
        int i = 0;
        for (AttackPattern<E> pattern : attacks) {
            pattern.fromSaveTag(tag.getCompound("Attack"+i));
            i++;
        }
    }
}
