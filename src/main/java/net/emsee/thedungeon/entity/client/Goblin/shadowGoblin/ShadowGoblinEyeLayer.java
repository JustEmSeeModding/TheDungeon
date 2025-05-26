package net.emsee.thedungeon.entity.client.Goblin.shadowGoblin;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.emsee.thedungeon.entity.custom.goblin.ShadowGoblinEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ShadowGoblinEyeLayer extends EyesLayer<ShadowGoblinEntity, ShadowGoblinModel> {
    private static final RenderType EYES = RenderType.eyes(TheDungeon.resourceLocation("textures/entity/goblin/shadow_goblin/shadow_goblin_eyes.png"));

    public ShadowGoblinEyeLayer(RenderLayerParent<ShadowGoblinEntity, ShadowGoblinModel> p_116964_) {
        super(p_116964_);
    }

    public @NotNull RenderType renderType() {
        return EYES;
    }
}
