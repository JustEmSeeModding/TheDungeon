package net.emsee.thedungeon.entity.client.Goblin.hobGoblin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HobGoblinRenderer extends MobRenderer<HobGoblinEntity, HobGoblinModel> {
    private static final Map<HobGoblinEntity.Variant, ResourceLocation> LOCATION_BY_VARIANTS= Util.make(Maps.newEnumMap(HobGoblinEntity.Variant.class), map -> {
        for (HobGoblinEntity.Variant variant : HobGoblinEntity.Variant.values()) {
            map.put(variant,
                    TheDungeon.defaultResourceLocation("textures/entity/goblin/hob_goblin/"+variant.getResource()+".png"));
        }
    });

    public HobGoblinRenderer(EntityRendererProvider.Context context) {
        super(context, new HobGoblinModel(context.bakeLayer(ModModelLayers.HOB_GOBLIN )), .5f);
        //this.addLayer(new HobGoblinEyeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HobGoblinEntity hobGoblinEntity) {
        return LOCATION_BY_VARIANTS.get(hobGoblinEntity.getVariant());
    }

    @Override
    protected void scale(@NotNull HobGoblinEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
