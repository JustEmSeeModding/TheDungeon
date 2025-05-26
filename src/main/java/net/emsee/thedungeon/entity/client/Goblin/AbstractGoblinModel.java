package net.emsee.thedungeon.entity.client.Goblin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.emsee.thedungeon.entity.custom.goblin.AbstractGoblinEntity;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class AbstractGoblinModel<T extends AbstractGoblinEntity> extends HierarchicalModel<T> implements ArmedModel {
    private final ModelPart base;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart left_arm;
    private final ModelPart lower_left_arm;
    private final ModelPart right_arm;
    private final ModelPart lower_right_arm;

    public AbstractGoblinModel(ModelPart root) {
        this.base = root.getChild("base");
        this.body = base.getChild("body");
        this.head = body.getChild("head");
        this.left_arm = body.getChild("left_arm");
        this.lower_left_arm = left_arm.getChild("lower_left_arm");
        this.right_arm = body.getChild("right_arm");
        this.lower_right_arm = right_arm.getChild("lower_right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = base.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 24).addBox(-3.0F, -9.5F, -1.5F, 6.0F, 9.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(-3.0F, -9.5F, -1.5F, 6.0F, 9.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -7.5F, -0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(24, 16).addBox(-1.0F, -3.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -9.5F, -0.5F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(24, 8).addBox(-7.0F, -4.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -4.0F, 0.0F, 0.0873F, 0.3927F, 0.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(24, 0).addBox(0.0F, -4.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.0F, 0.0F, 0.0873F, -0.3927F, 0.0F));

        PartDefinition left_ear2 = head.addOrReplaceChild("left_ear2", CubeListBuilder.create().texOffs(24, 10).addBox(3.0F, 1.0F, -3.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -10.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(30, 24).mirror().addBox(-0.5F, -2.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(18, 40).mirror().addBox(-0.5F, -2.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(3.5F, -7.5F, 0.0F));

        PartDefinition lower_left_arm = left_arm.addOrReplaceChild("lower_left_arm", CubeListBuilder.create().texOffs(30, 32).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(18, 48).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(1.0F, 3.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(18, 24).addBox(-2.5F, -2.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 40).addBox(-2.5F, -2.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(-3.5F, -7.5F, 0.0F));

        PartDefinition lower_right_arm = right_arm.addOrReplaceChild("lower_right_arm", CubeListBuilder.create().texOffs(18, 32).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(30, 48).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.0F, 3.0F, 0.0F));

        PartDefinition left_leg = base.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 24).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(42, 52).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.3F)).mirror(false)
                .texOffs(42, 38).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(1.5F, -8.0F, -0.5F));

        PartDefinition lower_left_leg = left_leg.addOrReplaceChild("lower_left_leg", CubeListBuilder.create().texOffs(42, 31).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(42, 45).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offset(0.0F, 4.0F, 0.0F));

        PartDefinition right_leg = base.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(54, 24).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(54, 52).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.3F))
                .texOffs(54, 38).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(-1.5F, -8.0F, -0.5F));

        PartDefinition lower_right_leg = right_leg.addOrReplaceChild("lower_right_leg", CubeListBuilder.create().texOffs(54, 31).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(54, 45).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 4.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(AbstractGoblinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.applyStatic(GoblinAnimations.ANIM_GOBLIN_POSE);
        this.animateWalk(entity.isRunning()? GoblinAnimations.ANIM_GOBLIN_RUN:GoblinAnimations.ANIM_GOBLIN_WALK, limbSwing, limbSwingAmount, 4f, 2.5f);
        this.animate(entity.idleAnimationState, GoblinAnimations.ANIM_GOBLIN_IDLE, ageInTicks, 1);
        this.animate(entity.basicAttackAnimationStateLeft, GoblinAnimations.ANIM_GOBLIN_BASIC_ATTACK_LEFT, ageInTicks, 1);
        this.animate(entity.basicAttackAnimationStateRight, GoblinAnimations.ANIM_GOBLIN_BASIC_ATTACK_RIGHT, ageInTicks, 1);
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
        body.translateAndRotate(poseStack);
        getArm(humanoidArm).translateAndRotate(poseStack);
        getLowerArm(humanoidArm).translateAndRotate(poseStack);
        poseStack.translate((humanoidArm==HumanoidArm.LEFT?-1:1)/16f,-4/16f,0);
        poseStack.scale(.85f, .85f, .85f);
    }
}
