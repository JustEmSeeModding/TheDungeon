package net.emsee.thedungeon.events;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinModel;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinRenderer;
import net.emsee.thedungeon.entity.client.Goblin.hobGoblin.HobGoblinModel;
import net.emsee.thedungeon.entity.client.Goblin.hobGoblin.HobGoblinRenderer;
import net.emsee.thedungeon.entity.client.Goblin.shadowGoblin.ShadowGoblinModel;
import net.emsee.thedungeon.entity.client.Goblin.shadowGoblin.ShadowGoblinRenderer;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.client.TestDummyModel;
import net.emsee.thedungeon.entity.client.TestDummyRenderer;
import net.emsee.thedungeon.entity.client.knight.deathKnight.DeathKnightModel;
import net.emsee.thedungeon.entity.client.knight.deathKnight.DeathKnightRenderer;
import net.emsee.thedungeon.entity.client.knight.skeletonKnight.SkeletonKnightModel;
import net.emsee.thedungeon.entity.client.knight.skeletonKnight.SkeletonKnightRenderer;
import net.emsee.thedungeon.entity.custom.TestDummyEntity;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.HobGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.emsee.thedungeon.entity.custom.knight.DeathKnightEntity;
import net.emsee.thedungeon.entity.custom.knight.SkeletonKnightEntity;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModEntityRegisterEvents {
    public static void ClientEntityRendererSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.TEST_DUMMY.get(), TestDummyRenderer::new);
        EntityRenderers.register(ModEntities.DEATH_KNIGHT.get(), DeathKnightRenderer::new);
        EntityRenderers.register(ModEntities.SKELETON_KNIGHT.get(), SkeletonKnightRenderer::new);
        EntityRenderers.register(ModEntities.CAVE_GOBLIN.get(), CaveGoblinRenderer::new);
        EntityRenderers.register(ModEntities.SHADOW_GOBLIN.get(), ShadowGoblinRenderer::new);
        EntityRenderers.register(ModEntities.HOB_GOBLIN.get(), HobGoblinRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.TEST_DUMMY, TestDummyModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.DEATH_KNIGHT, DeathKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SKELETON_KNIGHT, SkeletonKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CAVE_GOBLIN, CaveGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SHADOW_GOBLIN, ShadowGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HOB_GOBLIN, HobGoblinModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TEST_DUMMY.get(), TestDummyEntity.createAttributes().build());
        event.put(ModEntities.DEATH_KNIGHT.get(), DeathKnightEntity.createAttributes().build());
        event.put(ModEntities.SKELETON_KNIGHT.get(), SkeletonKnightEntity.createAttributes().build());
        event.put(ModEntities.CAVE_GOBLIN.get(), CaveGoblinEntity.createAttributes().build());
        event.put(ModEntities.SHADOW_GOBLIN.get(), ShadowGoblinEntity.createAttributes().build());
        event.put(ModEntities.HOB_GOBLIN.get(), HobGoblinEntity.createAttributes().build());
    }
}
