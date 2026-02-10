package net.emsee.thedungeon.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.emsee.thedungeon.TheDungeon;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = TheDungeon.MOD_ID)
public class ModShaders {
    public static ShaderInstance DUNGEON_PORTAL_UNSTABLE;
    public static ShaderInstance DUNGEON_PORTAL_F;
    public static ShaderInstance DUNGEON_PORTAL_E;
    public static ShaderInstance DUNGEON_PORTAL_D;
    public static ShaderInstance DUNGEON_PORTAL_C;
    public static ShaderInstance DUNGEON_PORTAL_B;
    public static ShaderInstance DUNGEON_PORTAL_A;
    public static ShaderInstance DUNGEON_PORTAL_S;
    public static ShaderInstance DUNGEON_PORTAL_SS;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException {
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(1f, 0f, 0f, 1f);
                    DUNGEON_PORTAL_UNSTABLE = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(0f, 0f, 1f, 1f);
                    DUNGEON_PORTAL_F = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(0f, 1f, 1f, 1f);
                    DUNGEON_PORTAL_E = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(0f, 1f, 0f, 1f);
                    DUNGEON_PORTAL_D = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(0f, 1f, .5f, 1f);
                    DUNGEON_PORTAL_C = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {

                    shader.safeGetUniform("VertexColor").set(1f, 1f, 0f, 1f);
                    DUNGEON_PORTAL_B = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(.5f, 0f, 1f, 1f);
                    DUNGEON_PORTAL_A = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(0f, .5f, 1f, 1f);
                    DUNGEON_PORTAL_S = shader;
                });
        event.registerShader(dungeonShader(event.getResourceProvider()),
                shader -> {
                    shader.safeGetUniform("VertexColor").set(.5f, 1f, 0f, 1f);
                    DUNGEON_PORTAL_SS = shader;
                });
    }

    private static ShaderInstance dungeonShader(ResourceProvider resourceProvider) throws IOException {
        return new ShaderInstance(
                resourceProvider,
                TheDungeon.defaultResourceLocation("rendertype_dungeon_portal"),
                DefaultVertexFormat.POSITION);
    }
}
