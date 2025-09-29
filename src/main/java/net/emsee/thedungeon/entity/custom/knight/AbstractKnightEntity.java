package net.emsee.thedungeon.entity.custom.knight;

import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.MultiAnimatedAttackGoal;
import net.emsee.thedungeon.entity.ai.RunToTargetGoal;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.entity.custom.interfaces.IBasicAnimatedEntity;
import net.emsee.thedungeon.entity.custom.interfaces.IMultiAttackAnimatedEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractKnightEntity extends DungeonPathfinderMob implements IBasicAnimatedEntity, IMultiAttackAnimatedEntity {
    protected static final EntityDataAccessor<Boolean> RUNNING =
            SynchedEntityData.defineId(AbstractKnightEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> ATTACK_ANIMATION_ID =
            SynchedEntityData.defineId(AbstractKnightEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Byte> ATTACK_ANIMATION_VERSION =
            SynchedEntityData.defineId(AbstractKnightEntity.class, EntityDataSerializers.BYTE);

    public final AnimationState idleAnimationState = new AnimationState();
    protected int idleAnimationTimeout = 0;
    public final AnimationState basicAttackAnimationState = new AnimationState();
    public final AnimationState quickAttackAnimationState = new AnimationState();
    public int attackAnimationTimeout = 0;
    protected int animationNetworkVersion = -1;


    public AbstractKnightEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
        this.addTargetGoals();
        this.setupAttackGoal();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new RunToTargetGoal(this,1.2f,10, 3.5f, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new DungeonTargetSelectorGoal(this, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    protected void setupAttackGoal() {
        this.goalSelector.addGoal(1, new MultiAnimatedAttackGoal<>(this, 1.2, true)
                .withAttack(0,15,5,1,1, 1)
                .withAttack(1,7,3,.5f,.25f, .9f)
        );
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected void SetupAnimations() {
        if (this.idleAnimationTimeout<=0) {
            this.idleAnimationTimeout = 59;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        if(this.isAttacking() && getAnimationID()>-1 && this.attackAnimationTimeout <=0 && animationNetworkVersion!= getAnimationVersion()){
            switch (getAnimationID()) {
                case 0: {
                    this.attackAnimationTimeout = 19;
                    this.basicAttackAnimationState.start(this.tickCount);
                    break;
                }
                case 1: {
                    this.attackAnimationTimeout = 9;
                    this.quickAttackAnimationState.start(this.tickCount);
                    break;
                }
            }
            animationNetworkVersion = getAnimationVersion();
        } else {
            --this.attackAnimationTimeout;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.SetupAnimations();
        }
    }

    @Override
    public boolean isPlayingAttackAnimation() {
        return attackAnimationTimeout >0;
    }

    @Override
    public void startRunning() {
        this.entityData.set(RUNNING, true);
    }

    @Override
    public void stopRunning() {
        this.entityData.set(RUNNING, false);
    }

    void setAnimationID(int id, byte version) {
        this.entityData.set(ATTACK_ANIMATION_ID, id);
        this.entityData.set(ATTACK_ANIMATION_VERSION, version);
    }

    int getAnimationID() {
        return this.entityData.get(ATTACK_ANIMATION_ID);
    }
    int getAnimationVersion() {
        return this.entityData.get(ATTACK_ANIMATION_VERSION);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(RUNNING, false);
        builder.define(ATTACK_ANIMATION_ID, -1);
        builder.define(ATTACK_ANIMATION_VERSION, (byte)-1);
    }

    @Override
    public boolean isRunning() {
        return this.entityData.get(RUNNING);
    }

    @Override
    public void attackAnimation(int id, byte version) {
        setAnimationID(id, version);
    }

    @Override
    public boolean isAttacking() {
        return getAnimationID() >-1;
    }
}
