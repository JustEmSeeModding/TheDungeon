package net.emsee.thedungeon.entity.client;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.TestDummyEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class TestDummyRenderer extends LivingEntityRenderer<TestDummyEntity, TestDummyModel> {


    public TestDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new TestDummyModel(context.bakeLayer(ModModelLayers.TEST_DUMMY)), .5f);
        //this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(TestDummyEntity testDummyEntity) {
        return TheDungeon.resourceLocation("textures/entity/test_dummy.png");
    }
}
