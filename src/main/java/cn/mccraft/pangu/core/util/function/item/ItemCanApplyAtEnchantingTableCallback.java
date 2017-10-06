package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemCanApplyAtEnchantingTableCallback {
  boolean apply(ItemStack arg0, Enchantment arg1);
}
