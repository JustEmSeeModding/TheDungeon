package net.emsee.thedungeon.block.entity.renderer.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.entity.custom.DungeonPortalBlockEntity;
import net.emsee.thedungeon.block.entity.renderer.ModBlockEntityModelLayers;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.emsee.thedungeon.renderer.ModRenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DungeonPortalBlockEntityRenderer implements BlockEntityRenderer<DungeonPortalBlockEntity> {
    public static final ResourceLocation textureLocation = TheDungeon.defaultResourceLocation("textures/block/portalblockentity.png");
    private final ModelPart base;
    //private final ModelPart block;



    public DungeonPortalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(ModBlockEntityModelLayers.DUNGEON_PORTAL);
        this.base = root.getChild("base");
        //this.block = base.getChild("block");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition block = base.addOrReplaceChild("block", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -14.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = block.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r2 = block.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r3 = block.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void render(DungeonPortalBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        DungeonRank rank = blockEntity.getRank();
        boolean isStable = blockEntity.isStable();
        boolean isExit = blockEntity.isExit();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(ModRenderType.dungeonPortal(rank, isStable, isExit));
        poseStack.pushPose();
        blockEntity.updateClientFrame();
        float scale = blockEntity.getAnimationScale();
        poseStack.translate(.5, blockEntity.getAnimationHeight(), .5);
        poseStack.scale(scale,scale,scale);
        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getAnimationRotation()));
        base.render(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }


    /*@Override
    public AABB getRenderBoundingBox(DungeonPortalBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return AABB.encapsulatingFullBlocks(pos.offset(-1, 0, -1), pos.offset(1, 1, 1));
    }*/
}
