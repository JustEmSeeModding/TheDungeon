package net.emsee.thedungeon.entity.custom;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.DungeonRunToTargetGoal;
import net.emsee.thedungeon.entity.ai.DungeonWalkToTargetGoal;
import net.emsee.thedungeon.entity.attack.AttackPattern;
import net.emsee.thedungeon.entity.attack.SimpleMeleeAttack;
import net.emsee.thedungeon.entity.brain.DungeonMobBrain;
import net.emsee.thedungeon.entity.client.animation.AnimationController;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonPathfinderMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TestBrainedEntity extends DungeonAnimatedMob {
    public final AnimationController animationController = new AnimationController()
            .withIdleAnimation(49)
            .withAttackAnimation(0,9) // right slash
            .withAttackAnimation(1,9) // left slash
            .withAttackAnimation(2,9); // both slash
    private final DungeonMobBrain<TestBrainedEntity> brain = new DungeonMobBrain<>(this);

    public TestBrainedEntity(EntityType<? extends DungeonPathfinderMob> type, Level level) {
        super(type, level);

        // Add attacks
        brain.addAttack(new SimpleMeleeAttack<>(0.5f, 0.75f, 0, 15, 1000, 5, AttackPattern.AttackHand.MAIN, new AttackPattern.HandPredicate.Empty(), new AttackPattern.HandPredicate.AlwaysTrue()));
        brain.addAttack(new SimpleMeleeAttack<>(0.5f, 0.75f, 1, 15, 1000, 5, AttackPattern.AttackHand.OFF));
        brain.addAttack(new SimpleMeleeAttack<>(0.7f, 0.5f, 2, 30, 100, 10, AttackPattern.AttackHand.BOTH));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0)
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE,3)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.MOVEMENT_SPEED, .27)
                .add(Attributes.KNOCKBACK_RESISTANCE, .1)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(ModAttributes.DUNGEON_MOB_REACH, 2.5)
                .add(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION, 500)
                .add(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION, 800);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
        this.addTargetGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new DungeonRunToTargetGoal(this,1.3f,15, 4f, true));
        this.goalSelector.addGoal(1, new DungeonWalkToTargetGoal(this, 1, 2,1,true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
    }

    protected void addTargetGoals() {
        this.targetSelector.addGoal(1, new DungeonTargetSelectorGoal(this, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            brain.tick(getTarget());
        } else {
            animationController.tick(this);
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