package committee.nova.prioritytarget.client

import committee.nova.prioritytarget.Enchantments.priorityTargetEnchantment
import committee.nova.prioritytarget.PriorityTarget
import committee.nova.prioritytarget.PriorityTarget.MODID
import committee.nova.prioritytarget.client.Overlay.{endRender, startRender, texture}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextComponentTranslation
import org.dimdev.rift.listener.client.OverlayRenderer

object Overlay {
  def startRender(): Unit = GlStateManager.enableBlend()

  def endRender(): Unit = GlStateManager.disableBlend()

  val texture = new ResourceLocation(MODID, "textures/overlay/indicator.png")
}

class Overlay extends OverlayRenderer {
  override def renderOverlay(): Unit = {
    val mc = Minecraft.getInstance()
    val player = mc.player
    val chestPlate = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
    val ptLevel = EnchantmentHelper.getEnchantmentLevel(priorityTargetEnchantment, chestPlate)
    if (ptLevel == 0) return
    val enemies = chestPlate.getOrCreateTag().getInt(PriorityTarget.MODID)
    if (enemies == 0) return
    val window = mc.mainWindow
    val baseW = window.getScaledWidth / 2F + 75
    val baseH = window.getScaledHeight / 2F - 65
    startRender()
    mc.fontRenderer.drawString("!", baseW - 12, baseH + 18, -39424)
    mc.fontRenderer.drawString(new TextComponentTranslation("overlay.prioritytarget.detected").getString, baseW - 30, baseH + 28, -39424)
    if (ptLevel > 1) {
      mc.getTextureManager.bindTexture(texture)
      Gui.drawModalRectWithCustomSizedTexture((baseW - 10).toInt, (baseH + 5).toInt, 0F, 0F, 16, 16, 16F, 16F)
      mc.fontRenderer.drawString(String.valueOf(enemies), baseW, baseH, -39424)
    }
    endRender()
  }
}
