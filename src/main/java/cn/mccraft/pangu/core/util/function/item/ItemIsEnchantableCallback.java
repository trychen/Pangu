package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemIsEnchantableCallback {
  boolean apply(ItemStack arg0);
}
