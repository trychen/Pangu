package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetUnlocalizedNameCallback {
  String apply(ItemStack arg0);
}
