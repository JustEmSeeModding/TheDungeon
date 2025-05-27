package net.emsee.thedungeon.entity.client.Goblin.shadowGoblin;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.Goblin.caveGoblin.CaveGoblinEyeLayer;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ShadowGoblinRenderer extends MobRenderer<ShadowGoblinEntity, ShadowGoblinModel> {
    public ShadowGoblinRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowGoblinModel(context.bakeLayer(ModModelLayers.SHADOW_GOBLIN )), .5f);
        this.addLayer(new ShadowGoblinEyeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ShadowGoblinEntity shadowGoblinEntity) {
        return TheDungeon.resourceLocation("textures/entity/goblin/shadow_goblin/shadow_goblin.png");
    }
}
