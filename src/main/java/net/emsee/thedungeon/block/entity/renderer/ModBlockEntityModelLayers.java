package net.emsee.thedungeon.block.entity.renderer;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModBlockEntityModelLayers {
    public static final ModelLayerLocation DUNGEON_PORTAL = new ModelLayerLocation(TheDungeon.defaultResourceLocation("dungeon_portal"), "main");

}
