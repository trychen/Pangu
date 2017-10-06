package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemSetDamageCallback {
  void apply(ItemStack arg0, int arg1);
}
