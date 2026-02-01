package net.emsee.thedungeon.entity.client.Goblin.shadowGoblin;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.Goblin.BrainTestModel;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.custom.TestBrainedEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BrainTestRenderer extends MobRenderer<TestBrainedEntity, BrainTestModel> {
    public BrainTestRenderer(EntityRendererProvider.Context context) {
        super(context, new BrainTestModel(context.bakeLayer(ModModelLayers.BRAIN_TEST )), .5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TestBrainedEntity brainedEntity) {
        return TheDungeon.defaultResourceLocation("textures/entity/goblin/shadow_goblin/shadow_goblin.png");
    }
}
