package net.emsee.thedungeon.entity.client.luminousCrawler;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.Goblin.shadowGoblin.ShadowGoblinModel;
import net.emsee.thedungeon.entity.custom.LuminousCrawlerEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LuminousCrawlerGlowLayer  extends EyesLayer<LuminousCrawlerEntity, LuminousCrawlerModel> {
        private static final RenderType EYES = RenderType.eyes(TheDungeon.defaultResourceLocation("textures/entity/luminous_crawler/luminous_crawler_glow.png"));

        public LuminousCrawlerGlowLayer(RenderLayerParent<LuminousCrawlerEntity, LuminousCrawlerModel> p_116964_) {
            super(p_116964_);
        }

        public @NotNull RenderType renderType() {
            return EYES;
        }
    }
