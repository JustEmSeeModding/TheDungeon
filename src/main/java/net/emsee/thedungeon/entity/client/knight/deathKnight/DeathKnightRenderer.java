package net.emsee.thedungeon.entity.client.knight.deathKnight;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.custom.knight.DeathKnightEntity;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DeathKnightRenderer extends MobRenderer<DeathKnightEntity, DeathKnightModel> {
    public DeathKnightRenderer(EntityRendererProvider.Context context) {
        super(context, new DeathKnightModel(context.bakeLayer(ModModelLayers.DEATH_KNIGHT)), .5f);
        this.addLayer(new DeathKnightEyeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DeathKnightEntity deathKnightEntity) {
        return TheDungeon.resourceLocation("textures/entity/knight/death_knight/death_knight.png");
    }

    @Override
    protected void scale(@NotNull DeathKnightEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(1.2F, 1.2F, 1.2F);
    }
}
