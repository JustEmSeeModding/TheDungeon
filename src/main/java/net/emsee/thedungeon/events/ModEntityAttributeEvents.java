package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.custom.CrystalGolemEntity;
import net.emsee.thedungeon.entity.custom.TestDummyEntity;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.emsee.thedungeon.entity.custom.knight.DeathKnightEntity;
import net.emsee.thedungeon.entity.custom.knight.SkeletonKnightEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModEntityAttributeEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TEST_DUMMY.get(), TestDummyEntity.createAttributes().build());
        event.put(ModEntities.DEATH_KNIGHT.get(), DeathKnightEntity.createAttributes().build());
        event.put(ModEntities.SKELETON_KNIGHT.get(), SkeletonKnightEntity.createAttributes().build());
        event.put(ModEntities.CAVE_GOBLIN.get(), CaveGoblinEntity.createAttributes().build());
        event.put(ModEntities.SHADOW_GOBLIN.get(), ShadowGoblinEntity.createAttributes().build());
        event.put(ModEntities.HOB_GOBLIN.get(), HobGoblinEntity.createAttributes().build());
        event.put(ModEntities.CRYSTAL_GOLEM.get(), CrystalGolemEntity.createAttributes().build());
    }
}
