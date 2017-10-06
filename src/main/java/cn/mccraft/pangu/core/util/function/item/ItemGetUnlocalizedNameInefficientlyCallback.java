package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetUnlocalizedNameInefficientlyCallback {
  String apply(ItemStack arg0);
}
