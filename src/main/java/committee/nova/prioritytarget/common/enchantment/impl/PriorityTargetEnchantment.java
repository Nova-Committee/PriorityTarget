package committee.nova.prioritytarget.common.enchantment.impl;

import committee.nova.prioritytarget.PriorityTarget;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PriorityTargetEnchantment extends Enchantment {
    public PriorityTargetEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isTreasureOnly() {
        return PriorityTarget.treasure.get();
    }
}
