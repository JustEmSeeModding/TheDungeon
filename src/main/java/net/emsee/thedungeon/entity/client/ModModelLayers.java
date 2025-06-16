package net.emsee.thedungeon.entity.client;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class ModModelLayers {
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
}
