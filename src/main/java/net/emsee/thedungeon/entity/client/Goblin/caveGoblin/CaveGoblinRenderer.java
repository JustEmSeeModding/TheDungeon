package net.emsee.thedungeon.entity.client.Goblin.caveGoblin;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModModelLayers;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CaveGoblinRenderer extends MobRenderer<CaveGoblinEntity, CaveGoblinModel> {
    public CaveGoblinRenderer(EntityRendererProvider.Context context) {
        super(context, new CaveGoblinModel(context.bakeLayer(ModModelLayers.CAVE_GOBLIN)), .5f);
        this.addLayer(new CaveGoblinEyeLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CaveGoblinEntity caveGoblinEntity) {
        return TheDungeon.resourceLocation("textures/entity/goblin/cave_goblin/cave_goblin.png");
    }
}
