package net.emsee.thedungeon.entity.client;

import net.emsee.thedungeon.TheDungeon;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public final class ModModelLayers {
    public static final ModelLayerLocation FLYING_BOOK = new ModelLayerLocation(
            TheDungeon.resourceLocation("flying_book_layer"), "main");

    public static final ModelLayerLocation TEST_DUMMY = new ModelLayerLocation(
            TheDungeon.resourceLocation("test_dummy"), "main");

    public static final ModelLayerLocation DEATH_KNIGHT = new ModelLayerLocation(
            TheDungeon.resourceLocation("death_knight"), "main");

    public static final ModelLayerLocation SKELETON_KNIGHT = new ModelLayerLocation(
            TheDungeon.resourceLocation("skeleton_knight"), "main");

    public static final ModelLayerLocation CAVE_GOBLIN = new ModelLayerLocation(
            TheDungeon.resourceLocation("cave_goblin"), "main");

    public static final ModelLayerLocation SHADOW_GOBLIN = new ModelLayerLocation(
            TheDungeon.resourceLocation("shadow_goblin"), "main");
}
