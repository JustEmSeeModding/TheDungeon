package net.emsee.thedungeon.entity.client.Goblin.hobGoblin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModEntityModelLayers;
import net.emsee.thedungeon.entity.custom.goblin.hobGoblin.HobGoblinEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HobGoblinRenderer extends MobRenderer<HobGoblinEntity, HobGoblinModel> {
    private static final Map<HobGoblinEntity.Variant, ResourceLocation> VARIANT_BASE_TEXTURE_OVERRIDES = Util.make(Maps.newEnumMap(HobGoblinEntity.Variant.class), map -> {
        //map.put(HobGoblinEntity.Variant.FIGHTER, TheDungeon.defaultResourceLocation("textures/entity/goblin/hob_goblin/variant/fighter.png"));
    });

    public HobGoblinRenderer(EntityRendererProvider.Context context) {
        super(context, new HobGoblinModel(context.bakeLayer(ModEntityModelLayers.HOB_GOBLIN )), .5f);
        //this.addLayer(new HobGoblinEyeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new HobGoblinVariantLayer(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HobGoblinEntity hobGoblinEntity) {
        ResourceLocation location = VARIANT_BASE_TEXTURE_OVERRIDES.get(hobGoblinEntity.getVariant());
        if (location == null) {
            location = TheDungeon.defaultResourceLocation("textures/entity/goblin/hob_goblin/hob_goblin.png");
        }
        return location;
    }

    @Override
    protected void scale(@NotNull HobGoblinEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
