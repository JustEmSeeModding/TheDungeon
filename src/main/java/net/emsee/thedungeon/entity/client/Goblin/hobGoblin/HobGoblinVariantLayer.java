package net.emsee.thedungeon.entity.client.Goblin.hobGoblin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModEntityModelLayers;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.minecraft.Util;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class HobGoblinVariantLayer extends RenderLayer<HobGoblinEntity, HobGoblinModel> {
    private final HobGoblinModel model;
    private static final Map<HobGoblinEntity.Variant, ResourceLocation> LOCATION_BY_VARIANTS= Util.make(Maps.newEnumMap(HobGoblinEntity.Variant.class), map -> {
        for (HobGoblinEntity.Variant variant : HobGoblinEntity.Variant.values()) {
            map.put(variant,
                    TheDungeon.defaultResourceLocation("textures/entity/goblin/hob_goblin/variant/"+variant.getResource()+".png"));
        }
    });

    public HobGoblinVariantLayer(RenderLayerParent<HobGoblinEntity, HobGoblinModel> renderer, EntityModelSet models) {
        super(renderer);
        model = new HobGoblinModel(models.bakeLayer(ModEntityModelLayers.HOB_GOBLIN_VARIANT));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, HobGoblinEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        HobGoblinEntity.Variant variant = entity.getVariant();
        ResourceLocation location = LOCATION_BY_VARIANTS.get(variant);
        if (location != null) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
            this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(location));
            this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
