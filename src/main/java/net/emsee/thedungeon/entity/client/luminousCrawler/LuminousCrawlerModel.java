package net.emsee.thedungeon.entity.client.luminousCrawler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emsee.thedungeon.entity.client.CrystalGolem.CrystalGolemAnimations;
import net.emsee.thedungeon.entity.custom.CrystalGolemEntity;
import net.emsee.thedungeon.entity.custom.LuminousCrawlerEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LuminousCrawlerModel extends HierarchicalModel<LuminousCrawlerEntity> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart base;
    private final ModelPart head;

    public LuminousCrawlerModel(ModelPart root) {
        this.base = root.getChild("base");
        this.head = this.base.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 17.0F, -1.35F));

        PartDefinition head = base.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 29).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.65F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 43).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition body = base.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.0F, -12.5F, 10.0F, 6.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.85F, -0.0436F, 0.0F, 0.0F));

        PartDefinition right_leg_2 = base.addOrReplaceChild("right_leg_2", CubeListBuilder.create().texOffs(24, 61).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 3.35F, 0.0F, -0.0873F, -0.1745F));

        PartDefinition right_leg_2_1 = right_leg_2.addOrReplaceChild("right_leg_2_1", CubeListBuilder.create().texOffs(0, 61).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

        PartDefinition right_leg_3 = base.addOrReplaceChild("right_leg_3", CubeListBuilder.create().texOffs(24, 57).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, -1.65F, 0.0F, 0.0873F, -0.1745F));

        PartDefinition right_leg_3_1 = right_leg_3.addOrReplaceChild("right_leg_3_1", CubeListBuilder.create().texOffs(0, 57).addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

        PartDefinition right_leg_4 = base.addOrReplaceChild("right_leg_4", CubeListBuilder.create().texOffs(22, 53).addBox(0.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, -6.65F, 0.0155F, 0.3487F, -0.1293F));

        PartDefinition right_leg_4_1 = right_leg_4.addOrReplaceChild("right_leg_4_1", CubeListBuilder.create().texOffs(0, 53).addBox(0.0F, -1.0F, -1.0F, 9.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.2217F));

        PartDefinition left_leg_2 = base.addOrReplaceChild("left_leg_2", CubeListBuilder.create().texOffs(48, 61).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, 3.35F, 0.0F, 0.0873F, 0.1745F));

        PartDefinition left_leg_2_1 = left_leg_2.addOrReplaceChild("left_leg_2_1", CubeListBuilder.create().texOffs(72, 61).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

        PartDefinition left_leg_3 = base.addOrReplaceChild("left_leg_3", CubeListBuilder.create().texOffs(48, 57).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, -1.65F, 0.0F, -0.0873F, 0.1745F));

        PartDefinition left_leg_3_1 = left_leg_3.addOrReplaceChild("left_leg_3_1", CubeListBuilder.create().texOffs(72, 57).addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

        PartDefinition left_leg_4 = base.addOrReplaceChild("left_leg_4", CubeListBuilder.create().texOffs(42, 53).addBox(-8.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 0.0F, -6.65F, 0.0155F, -0.3487F, 0.1293F));

        PartDefinition left_leg_4_1 = left_leg_4.addOrReplaceChild("left_leg_4_1", CubeListBuilder.create().texOffs(62, 53).addBox(-9.0F, -1.0F, -1.0F, 9.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.2217F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    @Override
    public ModelPart root() {
        return base;
    }

    @Override
    public void setupAnim(LuminousCrawlerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(entity.isRunning() ? LuminousCrawlerAnimations.RUN : LuminousCrawlerAnimations.WALK, limbSwing, limbSwingAmount, 4f, 2.5f);
        this.animate(entity.animationController.idleAnimationState, LuminousCrawlerAnimations.IDLE, ageInTicks, 1);
        this.animate(entity.animationController.attackAnimationStates.get(0).state(), LuminousCrawlerAnimations.BITE, ageInTicks, 1);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45f);

        this.head.yRot = headYaw * (Mth.PI / 180f);
        this.head.xRot = headPitch * (Mth.PI / 180f);
    }
}
