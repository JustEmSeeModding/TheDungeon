package net.emsee.thedungeon.entity.client.CrystalGolem;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emsee.thedungeon.entity.client.Goblin.GoblinAnimations;
import net.emsee.thedungeon.entity.custom.CrystalGolemEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class CrystalGolemModel extends HierarchicalModel<CrystalGolemEntity> implements ArmedModel {
    private final ModelPart base;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_arm;
    private final ModelPart right_arm;

    public CrystalGolemModel(ModelPart root) {
        this.base = root.getChild("base");
        this.body = this.base.getChild("body");
        this.head = this.body.getChild("head");
        this.left_arm = this.body.getChild("left_arm");
        this.right_arm = this.body.getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 6).addBox(-4.0F, -9.0F, -2.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, 9).addBox(-4.0F, -9.0F, 2.0F, 8.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, -6).addBox(2.0F, -9.0F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(32, -3).addBox(-2.0F, -9.0F, -3.0F, 0.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(14, 26).addBox(0.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -6.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -1.0F, -2.0F, 3.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -6.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(28, 31).addBox(-3.9F, 0.0F, -1.99F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(44, 31).addBox(-0.1F, 0.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(CrystalGolemEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(entity.isRunning() ? CrystalGolemAnimations.RUN : CrystalGolemAnimations.WALK, limbSwing, limbSwingAmount, 4f, 2.5f);
        this.animate(entity.animationController.idleAnimationState, CrystalGolemAnimations.IDLE, ageInTicks, 1);
        this.animate(entity.animationController.attackAnimationStates.get(0).state(), CrystalGolemAnimations.ATTACK, ageInTicks, 1);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45f);

        this.head.yRot = headYaw * (Mth.PI / 180f);
        this.head.xRot = headPitch * (Mth.PI / 180f);
    }

    /*@Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }*/

    private ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }



    @Override
    public ModelPart root() {
        return base;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        base.translateAndRotate(poseStack);
        body.translateAndRotate(poseStack);
        getArm(humanoidArm).translateAndRotate(poseStack); //(humanoidArm == HumanoidArm.LEFT ? -1 : 1)/ 16f // -6 / 16f
        poseStack.translate(0 , 0, 0);
        poseStack.scale(.85f, .85f, .85f);
    }
}
