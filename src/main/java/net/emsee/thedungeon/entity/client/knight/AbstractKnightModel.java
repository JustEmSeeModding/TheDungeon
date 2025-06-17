package net.emsee.thedungeon.entity.client.knight;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emsee.thedungeon.entity.custom.knight.AbstractKnightEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public abstract class AbstractKnightModel<T extends AbstractKnightEntity> extends HierarchicalModel<T> implements ArmedModel {
    private final ModelPart base;
    private final ModelPart head;
    private final ModelPart left_arm;
    private final ModelPart lower_left_arm;
    private final ModelPart right_arm;
    private final ModelPart lower_right_arm;

    public AbstractKnightModel(ModelPart root) {
        this.base = root.getChild("base");
        this.head = this.base.getChild("head");
        this.left_arm = this.base.getChild("left_arm");
        this.lower_left_arm = left_arm.getChild("lower_left_arm");
        this.right_arm = this.base.getChild("right_arm");
        this.lower_right_arm = right_arm.getChild("lower_right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = base.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition headwear = head.addOrReplaceChild("headwear", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_horn = head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(32, 26).addBox(0.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 18).addBox(2.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition right_horn = head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(40, 26).addBox(-2.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 18).addBox(-4.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.3491F, 0.0F, 0.0F));

        PartDefinition hawk = head.addOrReplaceChild("hawk", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

        PartDefinition body = base.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition bodywear = body.addOrReplaceChild("bodywear", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = base.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(24, 32).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(25, 72).addBox(0.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 52).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(5.0F, -22.0F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(24, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(25, 80).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(1.0F, 4.0F, 0.0F));

        PartDefinition right_arm = base.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(33, 72).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 52).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.2F)), PartPose.offset(-5.0F, -22.0F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(40, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(33, 80).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.0F, 4.0F, 0.0F));

        PartDefinition left_leg = base.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(56, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(41, 72).addBox(-0.9F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 52).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(1.9F, -12.0F, 0.0F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(56, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(41, 80).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(56, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(0.1F, 6.0F, 0.0F));

        PartDefinition right_leg = base.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(72, 32).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(49, 72).addBox(-1.1F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 52).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.9F, -12.0F, 0.0F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(72, 42).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(49, 80).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(72, 62).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.1F)), PartPose.offset(-0.1F, 6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }



    @Override
    public void setupAnim(AbstractKnightEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(entity.isRunning()?KnightAnimations.ANIM_KNIGHT_RUN:KnightAnimations.ANIM_KNIGHT_WALK, limbSwing, limbSwingAmount, 4f, 2.5f);
        this.animate(entity.idleAnimationState, KnightAnimations.ANIM_KNIGHT_IDLE, ageInTicks, 1);
        this.animate(entity.basicAttackAnimationState, KnightAnimations.ANIM_KNIGHT_BASIC_ATTACK, ageInTicks, 1);
        this.animate(entity.quickAttackAnimationState, KnightAnimations.ANIM_KNIGHT_BASIC_ATTACK, ageInTicks, 2);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw= Mth.clamp(headYaw, -30f, 30f);
        headPitch= Mth.clamp(headPitch, -25f, 45f);

        this.head.yRot = headYaw * (Mth.PI/180f);
        this.head.xRot = headPitch * (Mth.PI/180f);
    }

    /*@Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        base.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }*/

    private ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.left_arm : this.right_arm;
    }
    private ModelPart getLowerArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? this.lower_left_arm : this.lower_right_arm;
    }


    @Override
    public ModelPart root() {
        return base;
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        base.translateAndRotate(poseStack);
        getArm(humanoidArm).translateAndRotate(poseStack);
        getLowerArm(humanoidArm).translateAndRotate(poseStack);
        poseStack.translate((humanoidArm==HumanoidArm.LEFT?-1:1)/16f,-4/16f,0);
    }
}