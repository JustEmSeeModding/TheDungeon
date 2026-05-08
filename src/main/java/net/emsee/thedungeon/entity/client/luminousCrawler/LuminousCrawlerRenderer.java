package net.emsee.thedungeon.entity.client.luminousCrawler;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinEyeLayer;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinModel;
import net.emsee.thedungeon.entity.client.ModEntityModelLayers;
import net.emsee.thedungeon.entity.custom.LuminousCrawlerEntity;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.minecraft.client.renderer.entity.AllayRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.VexRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.allay.Allay;
import org.jetbrains.annotations.NotNull;

public class LuminousCrawlerRenderer extends MobRenderer<LuminousCrawlerEntity, LuminousCrawlerModel> {
    public LuminousCrawlerRenderer(EntityRendererProvider.Context context) {
        super(context, new LuminousCrawlerModel(context.bakeLayer(ModEntityModelLayers.LUMINOUS_CRAWLER)), .5f);
        this.addLayer(new LuminousCrawlerGlowLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LuminousCrawlerEntity luminousCrawlerEntity) {
        return TheDungeon.defaultResourceLocation("textures/entity/luminous_crawler/luminous_crawler.png");
    }

    /*@Override
    protected int getBlockLightLevel(LuminousCrawlerEntity entity, BlockPos pos) {
        return 15;
    }*/
}
