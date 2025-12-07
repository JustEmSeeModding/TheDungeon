package net.emsee.thedungeon.entity.custom.abstracts;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.damageType.ModDamageTypes;
import net.emsee.thedungeon.item.custom.DungeonWeaponItem;
import net.emsee.thedungeon.item.interfaces.IDungeonWeapon;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

//TODO make extend LivingEntity
public abstract class DungeonPathfinderMob extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(DungeonPathfinderMob.class, EntityDataSerializers.BOOLEAN);

    protected boolean finalizedSpawn = false;


    protected DungeonPathfinderMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public void resetEquipmentItems() {
        clearItems();
        populateDefaultEquipmentSlots(random, level().getCurrentDifficultyAt(blockPosition()));
    }

    public void clearItems() {
        getAllSlots().forEach(stack -> stack.setCount(0));
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
            double maxReach = this.getAttributeValue(ModAttributes.DUNGEON_MOB_REACH);
            float distance = pEnemy.distanceTo(this);
            return super.isWithinMeleeAttackRange(pEnemy) || (distance <= maxReach);
        } else
            return super.isWithinMeleeAttackRange(pEnemy);
        //return super.isWithinMeleeAttackRange(entity);
    }

    public boolean isWithinMeleeAttackRange(LivingEntity pEnemy, double maxReach) {
        if (this.getAttribute(ModAttributes.DUNGEON_MOB_REACH) != null) {
            float distance = pEnemy.distanceTo(this);
            return super.isWithinMeleeAttackRange(pEnemy) || (distance <= maxReach);
        } else
            return super.isWithinMeleeAttackRange(pEnemy);
        //return super.isWithinMeleeAttackRange(entity);
    }

    public boolean isWithinMeleeAttackRange(LivingEntity pEnemy, double minReach, double maxReach) {
        if (this.getAttribute(ModAttributes.DUNGEON_MOB_REACH) != null) {
            float distance = pEnemy.distanceTo(this);
            return super.isWithinMeleeAttackRange(pEnemy) || (distance <= maxReach && distance >= minReach);
        } else
            return super.isWithinMeleeAttackRange(pEnemy);
        //return super.isWithinMeleeAttackRange(entity);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (
            // allowed sources
                source.is(ModDamageTypes.DUNGEON_RESET) ||
                        source.is(ModDamageTypes.UNSTABLE_PORTAL) ||
                        source.is(DamageTypes.FELL_OUT_OF_WORLD) ||
                        source.is(DamageTypes.GENERIC_KILL) ||
                        source.is(ModDamageTypes.DUNGEON_WEAPON_TEST)
        ) return false;

        if (source.is(DamageTypes.PLAYER_ATTACK) && source.getEntity() instanceof Player player) {
            ItemStack handItem = player.getMainHandItem();
            return handItem.getCount()==0 || !(handItem.getItem() instanceof DungeonWeaponItem);
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

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        finalizedSpawn=true;
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}
