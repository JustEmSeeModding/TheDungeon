package net.emsee.thedungeon.entity.attack;


import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public class SimpleMeleeAttack<E extends DungeonAnimatedMob> implements AttackPattern<E> {
    private final float damageMultiplier;
    private final float knockbackMultiplier;
    private final int animationID;
    private final int duration;
    private final int cooldown;
    private final int hurtDelay;
    private final AttackHand hands;
    private final HandPredicate mainHandPredicate;
    private final HandPredicate offHandPredicate;
    private int remainingDuration = 0;
    private int remainingCooldown = 0;
    private boolean hasHurt = false;

    public SimpleMeleeAttack(float damageMultiplier, float knockbackMultiplier, int animationID, int duration, int cooldown, int hurtDelay, AttackHand hands) {
        this.damageMultiplier = damageMultiplier;
        this.knockbackMultiplier = knockbackMultiplier;
        this.animationID = animationID;
        this.duration = duration;
        this.cooldown = cooldown;
        this.hurtDelay = hurtDelay;
        this.hands = hands;
        this.mainHandPredicate = new HandPredicate.AlwaysTrue();
        this.offHandPredicate = new HandPredicate.AlwaysTrue();
    }

    public SimpleMeleeAttack(float damageMultiplier, float knockbackMultiplier, int animationID, int duration, int cooldown, int hurtDelay, AttackHand hands, HandPredicate mainHandPredicate, HandPredicate offHandPredicate) {
        this.damageMultiplier = damageMultiplier;
        this.knockbackMultiplier = knockbackMultiplier;
        this.animationID = animationID;
        this.duration = duration;
        this.cooldown = cooldown;
        this.hurtDelay = hurtDelay;
        this.hands = hands;
        this.mainHandPredicate = mainHandPredicate;
        this.offHandPredicate = offHandPredicate;
    }

    @Override
    public void start(E entity, LivingEntity target) {
        remainingDuration = duration;
        remainingCooldown = cooldown;
        hasHurt = false;
        switch (getHands()) {
            case MAIN -> {
                entity.swing(InteractionHand.MAIN_HAND);
            }
            case OFF -> {
                entity.swing(InteractionHand.OFF_HAND);
            }
            case BOTH -> {
                entity.swing(InteractionHand.MAIN_HAND);
                entity.swing(InteractionHand.OFF_HAND);
            }
        }
    }

    @Override
    public void tick(E entity, LivingEntity target) {
        remainingDuration--;
        if (remainingDuration <= duration-hurtDelay && !hasHurt) {
            if (target!= null && entity.isWithinMeleeAttackRange(target)) {
                target.hurt(entity.damageSources().mobAttack(entity), (float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * damageMultiplier);
            }
            hasHurt = true;
        }
    }

    @Override
    public void passiveTick(E entity) {
        remainingCooldown--;
    }

    @Override
    public boolean isFinished(E entity) {
        return remainingDuration <= 0;
    }

    @Override
    public float getDamageMultiplier() { return damageMultiplier; }

    @Override
    public float getKnockbackMultiplier() { return knockbackMultiplier; }

    @Override
    public AttackHand getHands() { return hands; }

    @Override
    public boolean canUseWithItems(ItemStack mainHand, ItemStack offHand) {
        return mainHandPredicate.test(mainHand) && offHandPredicate.test(offHand);
    }

    @Override
    public boolean canUseAgainstNow(E entity, LivingEntity target) {
        if (target==null) return false;
        return entity.isWithinMeleeAttackRange(target);
    }

    @Override
    public boolean canUseNow() {
        return remainingCooldown <= 0;
    }

    @Override
    public int getAnimationID() { return animationID; }

    @Override
    public int getAnimationDuration() { return duration; }

    @Override
    public CompoundTag toSaveTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Duration", remainingDuration);
        tag.putInt("Cooldown", remainingCooldown);
        tag.putBoolean("HasHurt", hasHurt);
        return tag;
    }

    @Override
    public void fromSaveTag(CompoundTag tag) {
        remainingDuration = tag.getInt("Duration");
        remainingCooldown = tag.getInt("Cooldown");
        hasHurt = tag.getBoolean("HasHurt");
    }
}
