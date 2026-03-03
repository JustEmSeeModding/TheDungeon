package net.emsee.thedungeon.entity.client;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ModEntityModelLayers {
    public static final ModelLayerLocation FLYING_BOOK = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("flying_book_layer"), "main");

    public static final ModelLayerLocation TEST_DUMMY = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("test_dummy"), "main");

    public static final ModelLayerLocation DEATH_KNIGHT = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("death_knight"), "main");

    public static final ModelLayerLocation SKELETON_KNIGHT = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("skeleton_knight"), "main");

    public static final ModelLayerLocation CAVE_GOBLIN = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("cave_goblin"), "main");

    public static final ModelLayerLocation SHADOW_GOBLIN = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("shadow_goblin"), "main");

    public static final ModelLayerLocation HOB_GOBLIN = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("hob_goblin"), "main");
    public static final ModelLayerLocation HOB_GOBLIN_VARIANT = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("hob_goblin_variant"), "variant");

    public static final ModelLayerLocation CRYSTAL_GOLEM = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("crystal_golem"), "main");

    public static final ModelLayerLocation LUMINOUS_CRAWLER = new ModelLayerLocation(
            TheDungeon.defaultResourceLocation("luminous_crawler"), "main");
}
