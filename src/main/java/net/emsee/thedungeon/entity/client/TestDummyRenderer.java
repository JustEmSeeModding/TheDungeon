package net.emsee.thedungeon.entity.client;

import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.custom.TestDummyEntity;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;

public class TestDummyRenderer extends LivingEntityRenderer<TestDummyEntity, TestDummyModel> {


    public TestDummyRenderer(EntityRendererProvider.Context context) {
        super(context, new TestDummyModel(context.bakeLayer(ModModelLayers.TEST_DUMMY)), .5f);
        //this.addLayer(new ItemInHandLayer(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(TestDummyEntity testDummyEntity) {
        return TheDungeon.defaultResourceLocation("textures/entity/test_dummy.png");
    }
}
