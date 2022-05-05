package committee.nova.prioritytarget.client.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.prioritytarget.PriorityTarget;
import committee.nova.prioritytarget.common.enchantment.init.EnchantmentInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Overlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void indicatorRender(RenderGameOverlayEvent.Pre event) {
        final Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        final ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chestplate.isEmpty()) return;
        final int level = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.spiderSense.get(), chestplate);
        if (level < 1) return;
        final int target = chestplate.getOrCreateTag().getInt("targeted_entities");
        if (target < 1) return;
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        final int h = event.getWindow().getGuiScaledHeight();
        final int w = event.getWindow().getGuiScaledWidth();
        startRender();
        final ResourceLocation texture = new ResourceLocation("prioritytarget:textures/overlay/indicator.png");
        final float baseW = w / 2F + 75;
        final float baseH = h / 2F - 65;
        if (level < 2 || !PriorityTarget.omitDetected.get()) {
            Minecraft.getInstance().font.draw(event.getMatrixStack(), new TextComponent("!")
                    , baseW - 12, baseH + 18, -39424);
            Minecraft.getInstance().font.draw(event.getMatrixStack(), new TranslatableComponent("overlay.prioritytarget.detected")
                    , baseW - 30, baseH + 28, -39424);
        }
        if (level >= 2) {
            RenderSystem.setShaderTexture(0, texture);
            GuiComponent.blit(event.getMatrixStack(), (int) (baseW - 10), (int) (baseH + 5), 0, 0, 16, 16, 16, 16);
            Minecraft.getInstance().font.draw(event.getMatrixStack(), new TextComponent(String.valueOf(target))
                    , baseW, baseH, -39424);
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
