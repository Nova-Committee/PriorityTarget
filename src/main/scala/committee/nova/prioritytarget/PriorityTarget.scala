package committee.nova.prioritytarget

import committee.nova.prioritytarget.Enchantments.priorityTargetEnchantment
import committee.nova.prioritytarget.PriorityTarget.{LOGGER, MODID}
import net.minecraft.enchantment.{Enchantment, EnchantmentHelper}
import net.minecraft.entity.monster.EntityMob
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.dimension.DimensionType
import org.apache.logging.log4j.LogManager
import org.dimdev.rift.listener.{EnchantmentAdder, ServerTickable}
import org.dimdev.riftloader.listener.InitializationListener

object PriorityTarget {
  final val MODID = "prioritytarget"
  final val LOGGER = LogManager.getLogger
}

class PriorityTarget extends InitializationListener with EnchantmentAdder with ServerTickable {
  override def onInitialization(): Unit = LOGGER.info("PriorityTarget activated!")

  override def registerEnchantments(): Unit = {
    Enchantment.register(MODID + ":" + MODID, priorityTargetEnchantment)
  }

  override def serverTick(server: MinecraftServer): Unit = {
    val time = server.getWorld(DimensionType.OVERWORLD).getDayTime
    if (time % 10 != 0) return
    val list = server.getPlayerList
    if (list == null) return
    val players = list.getPlayers
    if (players == null) return
    players.forEach(p => {
      if (p == null) return
      val c = p.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
      val l = EnchantmentHelper.getEnchantmentLevel(priorityTargetEnchantment, c)
      if (l == 0) return
      val target = p.world.getEntitiesWithinAABB(classOf[EntityMob],
        new AxisAlignedBB(p.getPositionVector.add(15, 15, 15), p.getPositionVector.add(-15, -15, -15)),
        (t: EntityMob) => p.equals(t.getAttackTarget)
      ).size()
      c.getOrCreateTag().putInt(MODID, target)
    })
  }
}
