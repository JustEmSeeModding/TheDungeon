package net.emsee.thedungeon.entity.attack;

import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.utils.ParticleUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParticleBeamRangedAttack<E extends DungeonAnimatedMob>  extends AbstractAttackPattern<E>{
    private final float damage;
    private final int animationID;
    private final int duration;
    private final int cooldown;
    private final int hurtDelay;

    private final float minRange;
    private final float maxRange;
    private final HandPredicate mainHandPredicate;
    private final HandPredicate offHandPredicate;
    private int remainingDuration = 0;
    private int remainingCooldown = 0;
    private boolean hasHurt = false;
    private final double particleDensity;

    private final ParticleOptions particleOptions;

    public ParticleBeamRangedAttack(float damage, float minRange, float maxRange, ParticleOptions particleOptions, double particleDensity , int animationID, int duration, int cooldown, int hurtDelay) {
        this.damage = damage;
        this.animationID = animationID;
        this.duration = duration;
        this.cooldown = cooldown;
        this.hurtDelay = hurtDelay;
        this.mainHandPredicate = new HandPredicate.AlwaysTrue();
        this.offHandPredicate = new HandPredicate.AlwaysTrue();
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.particleOptions = particleOptions;
        this.particleDensity = particleDensity;
    }

    public ParticleBeamRangedAttack(float damage, float minRange, float maxRange, ParticleOptions particleOptions, double particleDensity , int animationID, int duration, int cooldown, int hurtDelay, HandPredicate mainHandPredicate, HandPredicate offHandPredicate) {
        this.damage = damage;
        this.animationID = animationID;
        this.duration = duration;
        this.cooldown = cooldown;
        this.hurtDelay = hurtDelay;
        this.mainHandPredicate = mainHandPredicate;
        this.offHandPredicate = offHandPredicate;
        this.minRange = minRange;
        this.maxRange = maxRange;
        this.particleOptions = particleOptions;
        this.particleDensity = particleDensity;
    }

    @Override
    public void start(E entity, LivingEntity target) {
        remainingDuration = duration;
        remainingCooldown = cooldown;
        hasHurt = false;
    }

    @Override
    public void tick(E entity, LivingEntity target) {
        remainingDuration--;
        if (remainingDuration <= duration-hurtDelay && !hasHurt) {
            if (target != null && isInRange(entity, target) && hasLineOfSight(entity, target)) {
                applyDamage(entity, target);
                drawLine(entity, target);
            } else {
                drawMissLine(entity);
            }
            hasHurt = true;
        }
    }


    protected void applyDamage(E entity, LivingEntity target) {
        target.hurt(entity.damageSources().mobAttack(entity), damage);
    }

    private void drawLine (E entity, LivingEntity target) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        Vec3 start = entity.position().add(0,entity.getBbHeight()/2,0);
        Vec3 dif = target.position().add(0,target.getBbHeight()/2,0).subtract(start);
        float length = (float) dif.length();
        Vec3 direction = dif.normalize();

        ParticleUtils.line(start.x, start.y, start.z, direction, length, level, particleOptions, 0d, 0d, 0d, particleDensity);
    }

    private void drawMissLine (E entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        Vec3 start = entity.position();
        Vec3 end = getMissBeamEndpoint(entity);
        Vec3 direction = end.subtract(start).normalize();
        double length = start.distanceTo(end);

        ParticleUtils.line(start.x, start.y, start.z, direction, (float) length, level, particleOptions, 0d, 0d, 0d, particleDensity);
    }

    private Vec3 getMissBeamEndpoint(E entity) {
        if (!(entity.level() instanceof ServerLevel level)) {
            return entity.position().add(entity.getLookAngle().scale(maxRange));
        }

        Vec3 start = entity.position();
        Vec3 direction = entity.getLookAngle();
        Vec3 end = start.add(direction.scale(maxRange));

        BlockHitResult hitResult = level.clip(new ClipContext(
                start,
                end,
               ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                entity
        ));

        if (hitResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            return hitResult.getLocation();
        }

        return end;
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
    public float getDamage() { return damage; }

    @Override
    public @Nullable AttackHand getHands() { return null; }

    @Override
    public boolean canUseWithItems(ItemStack mainHand, ItemStack offHand) {
        return mainHandPredicate.test(mainHand) && offHandPredicate.test(offHand);
    }

    @Override
    public boolean canUseAgainstNow(E entity, LivingEntity target) {
        if (target == null) return false;
        float distance = entity.distanceTo(target);
        return isInRange(entity, target) && isOutsideMinRange(entity, target) && hasLineOfSight(entity, target);
    }

    protected boolean isOutsideMinRange(E entity, LivingEntity target) {
        float distance = entity.distanceTo(target);
        return (distance >= minRange);
    }

    protected boolean isInRange(E entity, LivingEntity target) {
        float distance = entity.distanceTo(target);
        return (distance <= maxRange);
    }

    protected boolean hasLineOfSight(E entity, LivingEntity target) {
        return entity.hasLineOfSight(target);
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
