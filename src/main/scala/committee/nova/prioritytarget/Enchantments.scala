package committee.nova.prioritytarget

import net.minecraft.enchantment.Enchantment.Rarity
import net.minecraft.enchantment.{Enchantment, EnumEnchantmentType}
import net.minecraft.inventory.EntityEquipmentSlot

object Enchantments {
  val priorityTargetEnchantment: Enchantment = new Enchantment(Rarity.RARE, EnumEnchantmentType.ARMOR_CHEST, Array(EntityEquipmentSlot.CHEST)) {
    override def getMaxLevel: Int = 2
  }
}
