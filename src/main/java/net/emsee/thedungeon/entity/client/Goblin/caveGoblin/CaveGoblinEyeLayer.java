package net.emsee.thedungeon.entity.client.Goblin.caveGoblin;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.goblin.CaveGoblinEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CaveGoblinEyeLayer extends EyesLayer<CaveGoblinEntity, CaveGoblinModel> {
    private static final RenderType EYES = RenderType.eyes(TheDungeon.resourceLocation("textures/entity/goblin/cave_goblin/cave_goblin_eyes.png"));

    public CaveGoblinEyeLayer(RenderLayerParent<CaveGoblinEntity, CaveGoblinModel> p_116964_) {
        super(p_116964_);
    }

    public @NotNull RenderType renderType() {
        return EYES;
    }
}
