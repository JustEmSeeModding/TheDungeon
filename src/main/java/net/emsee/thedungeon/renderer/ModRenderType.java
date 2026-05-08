package net.emsee.thedungeon.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.dungeon.src.DungeonRank;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModRenderType {
    public static final ResourceLocation DUNGEON_PORTAL_SKY_LOCATION = TheDungeon.defaultResourceLocation("textures/environment/dungeon_sky.png");
    public static final ResourceLocation DUNGEON_PORTAL_LOCATION = TheDungeon.defaultResourceLocation("textures/environment/dungeon_portal.png");

    public static RenderType dungeonPortal(DungeonRank rank, boolean stable, boolean exit) {
        return RenderType.create(
                "dungeon_portal",
                DefaultVertexFormat.POSITION,
                VertexFormat.Mode.QUADS,
                1536,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(ModRenderStateShard.dungeonPortal(rank, stable, exit))
                        .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                                .add(DUNGEON_PORTAL_SKY_LOCATION, false, false)
                                .add(DUNGEON_PORTAL_LOCATION, false, false)
                                .build())
                        .createCompositeState(false));
    }
}
