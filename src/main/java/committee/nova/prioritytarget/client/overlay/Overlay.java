package committee.nova.prioritytarget.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.prioritytarget.PriorityTarget;
import committee.nova.prioritytarget.api.ITargetablePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;


public class Overlay {
    public static void indicatorRender(Window window, GuiGraphics graphics) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) return;
        final int target = ((ITargetablePlayer) player).prioritytarget$getTargets();
        if (target == 0) return;
        final int h = window.getGuiScaledHeight();
        final int w = window.getGuiScaledWidth();
        startRender();
        final ResourceLocation texture = new ResourceLocation("prioritytarget:textures/overlay/indicator.png");
        final float baseW = w / 2F + 75;
        final float baseH = h / 2F - 65;
        final Font font = Minecraft.getInstance().font;
        if (target == -1 || !PriorityTarget.omitDetected.get()) {
            graphics.drawString(font, Component.literal("!"),
                    (int) (baseW - 12), (int) (baseH + 18), -39424, false);
            graphics.drawString(font, Component.translatable("overlay.prioritytarget.detected"),
                    (int) (baseW - 30), (int) (baseH + 28), -39424, false);
        }
        if (target != -1) {
            RenderSystem.setShaderTexture(0, texture);
            graphics.blit(texture, (int) (baseW - 10), (int) (baseH + 5), 0, 0, 16, 16, 16, 16);
            graphics.drawString(font, Component.literal(String.valueOf(target)),
                    (int) baseW, (int) baseH, -39424, false);
        }
        endRender();
    }

    private static void startRender() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void endRender() {
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
