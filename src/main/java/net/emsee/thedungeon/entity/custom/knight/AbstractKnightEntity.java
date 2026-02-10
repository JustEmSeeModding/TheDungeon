package net.emsee.thedungeon.entity.custom.knight;

import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.DungeonWalkToTargetGoal;
import net.emsee.thedungeon.entity.ai.DungeonRunToTargetGoal;
import net.emsee.thedungeon.entity.attack.AttackPattern;
import net.emsee.thedungeon.entity.attack.SimpleMeleeAttack;
import net.emsee.thedungeon.entity.brain.DungeonMobBrain;
import net.emsee.thedungeon.entity.client.animation.AnimationController;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.minecraft.nbt.CompoundTag;
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

public abstract class AbstractKnightEntity extends DungeonAnimatedMob{
    public final AnimationController animationController = new AnimationController()
            .withIdleAnimation(59)
            .withAttackAnimation(0,20) // slash
            .withAttackAnimation(1,10); // quick slash
    protected final DungeonMobBrain<AbstractKnightEntity> brain = new DungeonMobBrain<>(this);



    public AbstractKnightEntity(EntityType<? extends DungeonPathfinderMob> entityType, Level level) {
        super(entityType, level);
        setupBrain();
    }

    protected void setupBrain() {
        brain.addAttack(new SimpleMeleeAttack<>(1, 1, 0, 20, 40, 15, AttackPattern.AttackHand.MAIN));
        brain.addAttack(new SimpleMeleeAttack<>(0.5f, 0.25f, 1, 10, 30, 7, AttackPattern.AttackHand.OFF));

    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new DungeonRunToTargetGoal(this,1.2f,10, 3.5f, true));
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
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
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
