package net.emsee.thedungeon.entity;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.TestDummyEntity;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.emsee.thedungeon.entity.custom.knight.DeathKnightEntity;
import net.emsee.thedungeon.entity.custom.knight.SkeletonKnightEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TheDungeon.MOD_ID);

    public static final Supplier<EntityType<TestDummyEntity>> TEST_DUMMY =
            ENTITY_TYPES.register("test_dummy", () -> EntityType.Builder.of(TestDummyEntity::new, MobCategory.MISC)
                    .sized(1, 1.9f).build("test_dummy"));

    public static final Supplier<EntityType<DeathKnightEntity>> DEATH_KNIGHT =
            ENTITY_TYPES.register("death_knight", () -> EntityType.Builder.of(DeathKnightEntity::new, MobCategory.MONSTER)
                    .sized(.9f, 2.34f).build("death_knight"));

    public static final Supplier<EntityType<SkeletonKnightEntity>> SKELETON_KNIGHT =
            ENTITY_TYPES.register("skeleton_knight", () -> EntityType.Builder.of(SkeletonKnightEntity::new, MobCategory.MONSTER)
                    .sized(.75f, 1.98f).build("skeleton_knight"));

    public static final Supplier<EntityType<CaveGoblinEntity>> CAVE_GOBLIN =
            ENTITY_TYPES.register("cave_goblin", () -> EntityType.Builder.of(CaveGoblinEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.5f).build("cave_goblin"));

    public static final Supplier<EntityType<ShadowGoblinEntity>> SHADOW_GOBLIN =
            ENTITY_TYPES.register("shadow_goblin", () -> EntityType.Builder.of(ShadowGoblinEntity::new, MobCategory.MONSTER)
                    .sized(.6f, 1.5f).build("shadow_goblin"));

    public static final Supplier<EntityType<HobGoblinEntity>> HOB_GOBLIN =
            ENTITY_TYPES.register("hob_goblin", () -> EntityType.Builder.of(HobGoblinEntity::new, MobCategory.MONSTER)
                    .sized(.7f, 1.8f).build("hob_goblin"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
