package net.emsee.thedungeon.entity.custom.goblin;

import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.DungeonRunToTargetGoal;
import net.emsee.thedungeon.entity.ai.DungeonWalkToTargetGoal;
import net.emsee.thedungeon.entity.attack.AbstractAttackPattern;
import net.emsee.thedungeon.entity.attack.SimpleMeleeAttackDamageAttributeMultiplier;
import net.emsee.thedungeon.entity.brain.DungeonMobBrain;
import net.emsee.thedungeon.entity.client.animation.AnimationController;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.emsee.thedungeon.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGoblinEntity extends DungeonAnimatedMob {
    public final AnimationController animationController = new AnimationController()
            .withIdleAnimation(50)
            .withAttackAnimation(0,10) // right slash
            .withAttackAnimation(1,10) // left slash
            .withAttackAnimation(2,10); // both slash
    protected final DungeonMobBrain<AbstractGoblinEntity> brain = new DungeonMobBrain<>(this);



    public AbstractGoblinEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
        setupBrain();
    }

    protected void setupBrain() {
        brain.addAttack(new SimpleMeleeAttackDamageAttributeMultiplier<>(
                .5f,
                0,
                15,
                30,
                12,
                AbstractAttackPattern.AttackHand.MAIN));
        brain.addAttack(new SimpleMeleeAttackDamageAttributeMultiplier<>(
                .5f,
                1,
                15,
                30,
                12,
                AbstractAttackPattern.AttackHand.OFF));
        brain.addAttack(new SimpleMeleeAttackDamageAttributeMultiplier<>(
                1,
                2,
                30,
                60,
                12,
                AbstractAttackPattern.AttackHand.BOTH));
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new DungeonRunToTargetGoal(this,1.3f,15, 4f, true));
        this.goalSelector.addGoal(1, new DungeonWalkToTargetGoal(this,1f,1, 0, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new DungeonTargetSelectorGoal(this, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.KOBALT_DAGGER.get()));
        this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ModItems.KOBALT_DAGGER.get()));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.populateDefaultEquipmentSlots(random, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }


    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            animationController.tick(this);
        } else {
            brain.tick(this);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("CustomBrain", brain.toSaveTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        brain.fromSaveTag(tag.getCompound("CustomBrain"));
    }

}

