package net.emsee.thedungeon.entity.client.Goblin.hobGoblin;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.goblin.HobGoblinEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class HobGoblinEyeLayer extends EyesLayer<HobGoblinEntity, HobGoblinModel> {
    private static final RenderType EYES = RenderType.eyes(TheDungeon.defaultResourceLocation("textures/entity/goblin/hob_goblin/hob_goblin_eyes.png"));

    public HobGoblinEyeLayer(RenderLayerParent<HobGoblinEntity, HobGoblinModel> p_116964_) {
        super(p_116964_);
    }

    public @NotNull RenderType renderType() {
        return EYES;
    }
}
