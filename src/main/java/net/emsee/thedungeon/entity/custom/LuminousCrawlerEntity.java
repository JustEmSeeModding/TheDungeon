package net.emsee.thedungeon.entity.custom;

import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.ai.DungeonRunToTargetGoal;
import net.emsee.thedungeon.entity.ai.DungeonTargetSelectorGoal;
import net.emsee.thedungeon.entity.ai.DungeonWalkToTargetGoal;
import net.emsee.thedungeon.entity.attack.ParticleBeamRangedAttack;
import net.emsee.thedungeon.entity.attack.SimpleMeleeAttack;
import net.emsee.thedungeon.entity.brain.DungeonMobBrain;
import net.emsee.thedungeon.entity.client.animation.AnimationController;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.particle.ModParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LuminousCrawlerEntity extends DungeonAnimatedMob {
    public final AnimationController animationController = new AnimationController()
            .withIdleAnimation(100)
            .withAttackAnimation(0,10); // BITE attack
            //.withAttackAnimation(1,10)
            //.withAttackAnimation(2,10);
    protected final DungeonMobBrain<LuminousCrawlerEntity> brain =
                    new DungeonMobBrain<>(this)
                            .withAttack(new SimpleMeleeAttack<>(
                                    4,
                                    0,
                                    10,
                                    20,
                                    7,
                                    null))
                            .withAttack(new ParticleBeamRangedAttack<>(
                                    3,
                                    2.6f,
                                    10,
                                    ModParticleTypes.LUMINOUS_BEAM.get(),
                                    .4,
                                    0,
                                    10,
                                    40,
                                    7
                                    ));



    public LuminousCrawlerEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.ATTACK_DAMAGE,4)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.MOVEMENT_SPEED, .25)
                .add(Attributes.KNOCKBACK_RESISTANCE, .4)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(ModAttributes.DUNGEON_MOB_REACH, 2.5)
                .add(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION, 700)
                .add(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION, 1000);
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
