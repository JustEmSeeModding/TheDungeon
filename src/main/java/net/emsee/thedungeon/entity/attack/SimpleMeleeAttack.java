package net.emsee.thedungeon.entity.attack;


import net.emsee.thedungeon.entity.brain.DungeonMobAttackBrain;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleMeleeAttack<E extends DungeonAnimatedMob> extends AbstractAttackPattern<E> {
    protected final float damage;
    protected final int animationID;
    protected final int duration;
    protected final int cooldown;
    protected final int hurtDelay;
    protected final AttackHand hands;
    protected final HandPredicate mainHandPredicate;
    protected final HandPredicate offHandPredicate;
    protected int remainingDuration = 0;
    protected int remainingCooldown = 0;
    protected boolean hasHurt = false;

    public SimpleMeleeAttack(E entity,float damage, int animationID, int duration, int cooldown, int hurtDelay, AttackHand hands) {
        super(entity);
        this.damage = damage;
        this.animationID = animationID;
        this.duration = duration;
        this.cooldown = cooldown;
        this.hurtDelay = hurtDelay;
        this.hands = hands;
        this.mainHandPredicate = new HandPredicate.AlwaysTrue();
        this.offHandPredicate = new HandPredicate.AlwaysTrue();
    }

    public SimpleMeleeAttack(E entity,float damage, int animationID, int duration, int cooldown, int hurtDelay, AttackHand hands, HandPredicate mainHandPredicate, HandPredicate offHandPredicate) {
        super(entity);
        this.damage = damage;
        this.animationID = animationID;
        this.duration = duration;
        this.cooldown = cooldown;
        this.hurtDelay = hurtDelay;
        this.hands = hands;
        this.mainHandPredicate = mainHandPredicate;
        this.offHandPredicate = offHandPredicate;
    }

    @Override
    public void start(LivingEntity target) {
        remainingDuration = duration;
        remainingCooldown = cooldown;
        hasHurt = false;
        AttackHand hands = getHands();
        if (hands != null)
            switch (hands) {
                case MAIN ->
                        entity.swing(InteractionHand.MAIN_HAND);
                case OFF ->
                        entity.swing(InteractionHand.OFF_HAND);
                case BOTH -> {
                    entity.swing(InteractionHand.MAIN_HAND);
                    entity.swing(InteractionHand.OFF_HAND);
                }
            }
    }

    @Override
    public void tick(LivingEntity target) {
        remainingDuration--;
        if (remainingDuration <= duration-hurtDelay && !hasHurt) {
            if (target!= null && entity.isWithinMeleeAttackRange(target)) {
                applyDamage(target);
            }
            hasHurt = true;
        }
    }

    protected void applyDamage(LivingEntity target) {
        target.hurt(entity.damageSources().mobAttack(entity), damage);
    }

    @Override
    public void passiveTick() {
        remainingCooldown--;
    }

    @Override
    public boolean isFinished() {
        return remainingDuration <= 0;
    }

    @Override
    public float getDamage() { return damage; }

    @Override
    public @Nullable AttackHand getHands() { return hands; }

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
    public boolean canUseNow(E entity) {
        return remainingCooldown <= 0;
    }

    @Override
    public int getAnimationID() { return animationID; }

    @Override
    public int getAnimationDuration() { return duration; }

    @Override
    public @NotNull CompoundTag toSaveTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Duration", remainingDuration);
        tag.putInt("Cooldown", remainingCooldown);
        tag.putBoolean("HasHurt", hasHurt);
        return tag;
    }

    @Override
    public void fromSaveTag(@Nullable CompoundTag tag) {
        if (tag == null) return;
        remainingDuration = tag.getInt("Duration");
        remainingCooldown = tag.getInt("Cooldown");
        hasHurt = tag.getBoolean("HasHurt");
    }
}
