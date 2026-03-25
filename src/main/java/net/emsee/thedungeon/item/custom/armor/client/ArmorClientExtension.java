package net.emsee.thedungeon.item.custom.armor.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.item.custom.armor.client.model.ArmorModel;
import net.emsee.thedungeon.item.custom.armor.client.provider.IArmorModelProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ArmorClientExtension implements IClientItemExtensions {

    private final IArmorModelProvider provider;

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public ArmorClientExtension(IArmorModelProvider provider) {
        blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        this.provider = provider;
    }

    @Override
    public @NotNull ArmorModel getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
        ArmorModel armorModel = provider.getModel(living, stack, slot);
        armorModel.partVisible(slot);
        armorModel.crouching = original.crouching;
        armorModel.riding = original.riding;
        armorModel.young = original.young;
        return armorModel;
    }

    @Override
    public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        //fixes visibility bug because forge pain
        ArmorModel model = getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
        copyModelProperties(original, model);
        return model;
    }

    @SuppressWarnings("unchecked")
    private <T extends LivingEntity> void copyModelProperties(HumanoidModel<T> original, ArmorModel replacement) {
        original.copyPropertiesTo((HumanoidModel<T>) replacement);
        replacement.rightBoot.copyFrom(original.rightLeg);
        replacement.leftBoot.copyFrom(original.leftLeg);
        replacement.leggingsTop.copyFrom(original.body);
    }

}
