package net.emsee.thedungeon.events.entityCreation;



import net.emsee.thedungeon.entity.ModEntities;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinRenderer;
import net.emsee.thedungeon.entity.client.Goblin.hobGoblin.HobGoblinRenderer;
import net.emsee.thedungeon.entity.client.Goblin.shadowGoblin.ShadowGoblinRenderer;
import net.emsee.thedungeon.entity.client.knight.deathKnight.DeathKnightRenderer;
import net.emsee.thedungeon.entity.client.TestDummyRenderer;
import net.emsee.thedungeon.entity.client.knight.skeletonKnight.SkeletonKnightRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class ModEventBusClientEvents {
    public static void ClientEntityRendererSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.TEST_DUMMY.get(), TestDummyRenderer::new);
        EntityRenderers.register(ModEntities.DEATH_KNIGHT.get(), DeathKnightRenderer::new);
        EntityRenderers.register(ModEntities.SKELETON_KNIGHT.get(), SkeletonKnightRenderer::new);
        EntityRenderers.register(ModEntities.CAVE_GOBLIN.get(), CaveGoblinRenderer::new);
        EntityRenderers.register(ModEntities.SHADOW_GOBLIN.get(), ShadowGoblinRenderer::new);
        EntityRenderers.register(ModEntities.HOB_GOBLIN.get(), HobGoblinRenderer::new);
    }
}
