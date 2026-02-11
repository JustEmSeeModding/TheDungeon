package net.emsee.thedungeon.entity.custom;


import net.emsee.thedungeon.attribute.ModAttributes;
import net.emsee.thedungeon.entity.ai.DungeonRunToTargetGoal;
import net.emsee.thedungeon.entity.ai.DungeonWalkToTargetGoal;
import net.emsee.thedungeon.entity.attack.AttackPattern;
import net.emsee.thedungeon.entity.attack.SimpleMeleeAttack;
import net.emsee.thedungeon.entity.brain.DungeonMobBrain;
import net.emsee.thedungeon.entity.client.animation.AnimationController;
import net.emsee.thedungeon.entity.custom.abstracts.DungeonAnimatedMob;
import net.emsee.thedungeon.loot.ModLootTables;
import net.emsee.thedungeon.utils.WeightedMap;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;

public class CrystalGolemEntity extends DungeonAnimatedMob {
    public AnimationController animationController =  new AnimationController()
            .withIdleAnimation(23)
            .withAttackAnimation(0,12);
    protected DungeonMobBrain<CrystalGolemEntity> brain = new DungeonMobBrain<>(this);

    protected static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(CrystalGolemEntity.class, EntityDataSerializers.INT);

    private static final WeightedMap.Int<Variant> variants = Util.make(new WeightedMap.Int<>(), map -> {
        for (Variant variant : Variant.values()) {
            map.put(variant, variant.weight);
        }
    });

    public CrystalGolemEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        setupBrain();
    }

    protected void setupBrain() {
        brain.addAttack(new SimpleMeleeAttack<>(1f, 1f, 0, 12, 15, 6, AttackPattern.AttackHand.BOTH));
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
        //this.targetSelector.addGoal(1, new DungeonTargetSelectorGoal(this, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 50.0)
                .add(Attributes.MAX_HEALTH, 25)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.ATTACK_DAMAGE,2)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5)
                .add(Attributes.MOVEMENT_SPEED, .26)
                .add(Attributes.KNOCKBACK_RESISTANCE, .4)
                .add(Attributes.STEP_HEIGHT, 1)
                .add(ModAttributes.DUNGEON_MOB_REACH, 2)
                .add(ModAttributes.DUNGEON_MOB_MIN_PERCEPTION, 500)
                .add(ModAttributes.DUNGEON_MOB_MAX_PERCEPTION, 800);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        //this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.PYRITE.get()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT,0);
    }

    private int getTypeVariant() {
        return this.entityData.get(VARIANT);
    }

    public Variant getVariant() {
        return Variant.getById(this.getTypeVariant() & 255);
    }


    public void setVariant(Variant variant) {
        this.entityData.set(VARIANT, variant.getId() & 255);
        if (finalizedSpawn) {
            resetEquipmentItems();
        }
    }

    public void setRandomVariant() {
        Variant variant = variants.getRandom(this.random);
        this.setVariant(variant);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setRandomVariant();
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
        tag.putInt("Variant", this.getTypeVariant());
        tag.put("CustomBrain", brain.toSaveTag());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(VARIANT, tag.getInt("Variant"));
        brain.fromSaveTag(tag.getCompound("CustomBrain"));
    }


    @Override
    protected ResourceKey<LootTable> getDefaultLootTable() {
        return switch (getVariant()) {
            case AMETHYST -> ModLootTables.CRYSTAL_GOLEM_AMETHYST;
            case ROSELITH -> ModLootTables.CRYSTAL_GOLEM_ROSELITH;
            case GARNETORE -> ModLootTables.CRYSTAL_GOLEM_GARNETORE;
            case VERDANTITE -> ModLootTables.CRYSTAL_GOLEM_VERDANTITE;
            case LUMANITE -> ModLootTables.CRYSTAL_GOLEM_LUMANITE;
            case DIAMOND -> ModLootTables.CRYSTAL_GOLEM_DIAMOND;
            case EMERALD -> ModLootTables.CRYSTAL_GOLEM_EMERALD;
        };
    }


    public enum Variant {
        AMETHYST(1, 300, "amethyst"),
        ROSELITH(2, 500, "roselith"),
        GARNETORE(3, 300, "garnetore"),
        VERDANTITE(4, 200, "verdantite"),
        LUMANITE(5, 400, "lumanite"),
        DIAMOND(6, 10, "diamond"),
        EMERALD(7, 5, "emerald");

        private static final Variant[] BY_ID = Arrays.stream(values()).sorted(
                Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final int weight;
        private final String resource;

        Variant(int id, int weight, String resource) {
            this.id = id;
            this.weight = weight;
            this.resource = resource;
        }

        public int getId() {
            return id;
        }

        public static Variant getById(int id) {
            return BY_ID[id % BY_ID.length];
        }

        public String getResource() {
            return resource;
        }

        public int getWeight() {
            return weight;
        }
    }
}
