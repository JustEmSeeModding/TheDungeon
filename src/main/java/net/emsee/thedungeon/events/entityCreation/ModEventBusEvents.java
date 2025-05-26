package net.emsee.thedungeon.events.entityCreation;


import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinModel;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.client.TestDummyModel;
import net.emsee.thedungeon.entity.client.knight.deathKnight.DeathKnightModel;
import net.emsee.thedungeon.entity.client.knight.skeletonKnight.SkeletonKnightModel;
import net.emsee.thedungeon.entity.custom.TestDummyEntity;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.emsee.thedungeon.entity.custom.knight.DeathKnightEntity;
import net.emsee.thedungeon.entity.custom.knight.SkeletonKnightEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.TEST_DUMMY, TestDummyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.DEATH_KNIGHT, DeathKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SKELETON_KNIGHT, SkeletonKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CAVE_GOBLIN, CaveGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SHADOW_GOBLIN, CaveGoblinModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TEST_DUMMY.get(), TestDummyEntity.createAttributes().build());
        event.put(ModEntities.DEATH_KNIGHT.get(), DeathKnightEntity.createAttributes().build());
        event.put(ModEntities.SKELETON_KNIGHT.get(), SkeletonKnightEntity.createAttributes().build());
        event.put(ModEntities.CAVE_GOBLIN.get(), CaveGoblinEntity.createAttributes().build());
        event.put(ModEntities.SHADOW_GOBLIN.get(), ShadowGoblinEntity.createAttributes().build());
    }
}
