package net.emsee.thedungeon.entity.client.CrystalGolem;

import com.google.common.collect.Maps;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.entity.client.ModEntityModelLayers;
import net.emsee.thedungeon.entity.custom.CrystalGolemEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CrystalGolemRenderer extends MobRenderer<CrystalGolemEntity, CrystalGolemModel> {
    private static final Map<CrystalGolemEntity.Variant, ResourceLocation> LOCATION_BY_VARIANTS= Util.make(Maps.newEnumMap(CrystalGolemEntity.Variant.class), map -> {
        for (CrystalGolemEntity.Variant variant : CrystalGolemEntity.Variant.values()) {
            map.put(variant,
                    TheDungeon.defaultResourceLocation("textures/entity/crystal_golem/variant/"+variant.getResource()+".png"));
        }
    });

    public CrystalGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CrystalGolemModel(context.bakeLayer(ModEntityModelLayers.CRYSTAL_GOLEM )), .5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CrystalGolemEntity entity) {
        return LOCATION_BY_VARIANTS.get(entity.getVariant());
    }
}
