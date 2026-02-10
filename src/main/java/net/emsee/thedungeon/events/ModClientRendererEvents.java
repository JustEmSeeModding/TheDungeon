package net.emsee.thedungeon.events;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.entity.ModBlockEntities;
import net.emsee.thedungeon.block.entity.renderer.ModBlockEntityModelLayers;
import net.emsee.thedungeon.block.entity.renderer.custom.DungeonPortalBlockEntityRenderer;
import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.client.CrystalGolem.CrystalGolemModel;
import net.emsee.thedungeon.entity.client.CrystalGolem.CrystalGolemRenderer;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinModel;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinRenderer;
import net.emsee.thedungeon.entity.client.Goblin.hobGoblin.HobGoblinModel;
import net.emsee.thedungeon.entity.client.Goblin.hobGoblin.HobGoblinRenderer;
import net.emsee.thedungeon.entity.client.Goblin.shadowGoblin.ShadowGoblinModel;
import net.emsee.thedungeon.entity.client.Goblin.shadowGoblin.ShadowGoblinRenderer;
import net.emsee.thedungeon.entity.client.ModEntityModelLayers;
import net.emsee.thedungeon.entity.client.TestDummyModel;
import net.emsee.thedungeon.entity.client.TestDummyRenderer;
import net.emsee.thedungeon.entity.client.knight.deathKnight.DeathKnightModel;
import net.emsee.thedungeon.entity.client.knight.deathKnight.DeathKnightRenderer;
import net.emsee.thedungeon.entity.client.knight.skeletonKnight.SkeletonKnightModel;
import net.emsee.thedungeon.entity.client.knight.skeletonKnight.SkeletonKnightRenderer;
import net.emsee.thedungeon.renderer.ModRenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterNamedRenderTypesEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModClientRendererEvents {
    public static void ClientEntityRendererSetup(FMLClientSetupEvent event) {
        //Entities
        EntityRenderers.register(ModEntities.TEST_DUMMY.get(), TestDummyRenderer::new);
        EntityRenderers.register(ModEntities.DEATH_KNIGHT.get(), DeathKnightRenderer::new);
        EntityRenderers.register(ModEntities.SKELETON_KNIGHT.get(), SkeletonKnightRenderer::new);
        EntityRenderers.register(ModEntities.CAVE_GOBLIN.get(), CaveGoblinRenderer::new);
        EntityRenderers.register(ModEntities.SHADOW_GOBLIN.get(), ShadowGoblinRenderer::new);
        EntityRenderers.register(ModEntities.HOB_GOBLIN.get(), HobGoblinRenderer::new);
        EntityRenderers.register(ModEntities.CRYSTAL_GOLEM.get(), CrystalGolemRenderer::new);

        //BlockEntities
        BlockEntityRenderers.register(ModBlockEntities.DUNGEON_PORTAL_BLOCK_ENTITY.get(), DungeonPortalBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //Entities
        event.registerLayerDefinition(ModEntityModelLayers.TEST_DUMMY, TestDummyModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.DEATH_KNIGHT, DeathKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.SKELETON_KNIGHT, SkeletonKnightModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.CAVE_GOBLIN, CaveGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.SHADOW_GOBLIN, ShadowGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.HOB_GOBLIN, HobGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.HOB_GOBLIN_VARIANT, HobGoblinModel::createBodyLayer);
        event.registerLayerDefinition(ModEntityModelLayers.CRYSTAL_GOLEM, CrystalGolemModel::createBodyLayer);

        //BlockEntities
        event.registerLayerDefinition(ModBlockEntityModelLayers.DUNGEON_PORTAL, DungeonPortalBlockEntityRenderer::createBodyLayer);
    }
}
