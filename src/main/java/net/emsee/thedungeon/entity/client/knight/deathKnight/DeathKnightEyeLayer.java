package net.emsee.thedungeon.entity.client.knight.deathKnight;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.knight.DeathKnightEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DeathKnightEyeLayer extends EyesLayer<DeathKnightEntity, DeathKnightModel> {
    private static final RenderType EYES = RenderType.eyes(TheDungeon.defaultResourceLocation("textures/entity/knight/death_knight/death_knight_eyes.png"));

    public DeathKnightEyeLayer(RenderLayerParent<DeathKnightEntity, DeathKnightModel> p_116964_) {
        super(p_116964_);
    }

    public @NotNull RenderType renderType() {
        return EYES;
    }
}
