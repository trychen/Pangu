package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemHasCustomEntityCallback {
  boolean apply(ItemStack arg0);
}
