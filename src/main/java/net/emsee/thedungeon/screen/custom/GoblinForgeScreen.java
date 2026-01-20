package net.emsee.thedungeon.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.emsee.thedungeon.TheDungeon;
import net.emsee.thedungeon.block.entity.custom.GoblinForgeBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GoblinForgeScreen extends AbstractContainerScreen<GoblinForgeMenu> {
    private static final ResourceLocation GUI_TEXTURE = TheDungeon.defaultResourceLocation("textures/gui/goblin_forge/goblin_forge_gui.png");
    private static final ResourceLocation ARROW_TEXTURE = TheDungeon.defaultResourceLocation("textures/gui/goblin_forge/arrow_progress.png");
    private static final ResourceLocation FLAME_TEXTURE = TheDungeon.defaultResourceLocation("textures/gui/goblin_forge/flame.png");

    public GoblinForgeScreen(GoblinForgeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        renderProgressArrow(guiGraphics, x, y);
        renderFuelFlame(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(ARROW_TEXTURE, x+79, y+35, 0, 0, menu.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    private void renderFuelFlame(GuiGraphics guiGraphics, int x, int y) {
        if (menu.hasFuel()) {
            guiGraphics.blit(FLAME_TEXTURE, x+56, y+35+16-menu.getScaledFlameHeight(), 0, 16-menu.getScaledFlameHeight(), 16, menu.getScaledFlameHeight(), 16, 16);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
