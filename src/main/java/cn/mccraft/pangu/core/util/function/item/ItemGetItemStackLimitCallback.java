package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetItemStackLimitCallback {
  int apply(ItemStack arg0);
}
