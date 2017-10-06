package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetDurabilityForDisplayCallback {
  double apply(ItemStack arg0);
}
