package net.emsee.thedungeon.entity.client.knight.skeletonKnight;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.custom.knight.SkeletonKnightEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SkeletonKnightRenderer extends MobRenderer<SkeletonKnightEntity, SkeletonKnightModel> {
    public SkeletonKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonKnightModel(context.bakeLayer(ModModelLayers.SKELETON_KNIGHT)), .5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SkeletonKnightEntity skeletonKnightEntity) {
        return TheDungeon.resourceLocation("textures/entity/knight/skeleton_knight.png");
    }
}
