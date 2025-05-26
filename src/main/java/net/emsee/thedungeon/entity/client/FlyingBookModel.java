package net.emsee.thedungeon.entity.client;


/*
public class FlyingBookModel<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart bone;



	public FlyingBookModel(ModelPart root)
	{
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Left = body.addOrReplaceChild("Left", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition CoverLeft = Left.addOrReplaceChild("CoverLeft", CubeListBuilder.create(), PartPose.offset(0.0F, -0.4F, 1.0F));

		PartDefinition cube_r1 = CoverLeft.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, -5.0F, 6.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.3927F));

		PartDefinition PagesLeft = Left.addOrReplaceChild("PagesLeft", CubeListBuilder.create(), PartPose.offset(0.0F, -0.4F, 1.0F));

		PartDefinition Page3_r1 = PagesLeft.addOrReplaceChild("Page3_r1", CubeListBuilder.create().texOffs(2, 32).addBox(0.3F, -0.3F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(2, 32).addBox(0.2F, -0.2F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(2, 32).addBox(0.1F, -0.1F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.3927F));

		PartDefinition Right = body.addOrReplaceChild("Right", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition CoverRight = Right.addOrReplaceChild("CoverRight", CubeListBuilder.create(), PartPose.offset(0.0F, -0.4F, 1.0F));

		PartDefinition cube_r2 = CoverRight.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 11).addBox(-6.0F, 0.0F, -5.0F, 6.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -0.3927F));

		PartDefinition PagesRight = Right.addOrReplaceChild("PagesRight", CubeListBuilder.create(), PartPose.offset(0.0F, -0.4F, 1.0F));

		PartDefinition Page2_r1 = PagesRight.addOrReplaceChild("Page2_r1", CubeListBuilder.create().texOffs(2, 22).addBox(-5.2F, -0.2F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(2, 22).addBox(-5.1F, -0.1F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(2, 22).addBox(-5.3F, -0.3F, -4.5F, 5.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, -0.3927F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//todo
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return bone;
	}
}*/