package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemIsBeaconPaymentCallback {
  boolean apply(ItemStack arg0);
}
