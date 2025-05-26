package net.emsee.thedungeon.entity.custom.abstracts;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.item.interfaces.IDungeonWeapon;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

//TODO make extend LivingEntity
public abstract class DungeonPathfinderMob extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(DungeonPathfinderMob.class, EntityDataSerializers.BOOLEAN);


    protected DungeonPathfinderMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }


    /*@Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.isCreativePlayer() || source.is(ModDamageTypes.DUNGEON_WEAPON))
            return super.hurt(source, amount);
        return false;
    }*/

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity pEnemy) {
        if (this.getAttribute(ModAttributes.DUNGEON_MOB_REACH) != null) {
            double range = this.getAttributeValue(ModAttributes.DUNGEON_MOB_REACH);
            float distance = pEnemy.distanceTo(this);
            return super.isWithinMeleeAttackRange(pEnemy) || (distance <= range);
        } else
            return super.isWithinMeleeAttackRange(pEnemy);
        //return super.isWithinMeleeAttackRange(entity);
    }

    public boolean isWithinMeleeAttackRange(LivingEntity pEnemy, float reachMultiplier) {
        if (this.getAttribute(ModAttributes.DUNGEON_MOB_REACH) != null) {
            double range = this.getAttributeValue(ModAttributes.DUNGEON_MOB_REACH) * reachMultiplier;
            float distance = pEnemy.distanceTo(this);
            return super.isWithinMeleeAttackRange(pEnemy) || (distance <= range);
        } else
            return super.isWithinMeleeAttackRange(pEnemy);
        //return super.isWithinMeleeAttackRange(entity);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (
                source.is(ModDamageTypes.DUNGEON_RESET) ||
                        source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                        source.is(DamageTypes.GENERIC_KILL)
        ) return false;
        if (
                source.is(DamageTypes.ARROW) ||
                source.is(DamageTypes.TRIDENT) ||
                source.is(DamageTypes.FIREWORKS) ||
                source.is(DamageTypes.MOB_PROJECTILE) ||
                source.is(DamageTypes.PLAYER_EXPLOSION) ||
                source.is(DamageTypes.THROWN) ||
                source.is(DamageTypes.MAGIC)
        ) return true;
        if (source.getEntity() instanceof LivingEntity livingEntity) {
            ItemStack mainHandStack = livingEntity.getItemBySlot(EquipmentSlot.MAINHAND);
            if (mainHandStack.isEmpty()) return false;
            if (mainHandStack.getItem() instanceof IDungeonWeapon) return false;
        }
        return true;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }


    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlot slot) {
        return 0;
    }

    public boolean doHurtTargetMultiplier(LivingEntity entity, float damageMultiplier, float knockbackMultiplier) {
        float damage = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        DamageSource damagesource = this.damageSources().mobAttack(this);
        Level level = this.level();
        if (level instanceof ServerLevel serverlevel) {
            damage = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, damage);
        }

        boolean flag = entity.hurt(damagesource, damage * damageMultiplier);
        if (flag) {
            float knockback = this.getKnockback(entity, damagesource)*knockbackMultiplier;
            if (knockback > 0.0F && entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)entity;
                livingentity.knockback((double)(knockback * 0.5F), (double) Mth.sin(this.getYRot() * 0.017453292F), (double)(-Mth.cos(this.getYRot() * 0.017453292F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            }

            Level var7 = this.level();
            if (var7 instanceof ServerLevel) {
                ServerLevel serverlevel1 = (ServerLevel)var7;
                EnchantmentHelper.doPostAttackEffects(serverlevel1, entity, damagesource);
            }

            this.setLastHurtMob(entity);
            this.playAttackSound();
        }

        return flag;
    }
}
