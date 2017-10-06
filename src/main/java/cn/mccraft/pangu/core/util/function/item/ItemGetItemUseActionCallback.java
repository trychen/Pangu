package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetItemUseActionCallback {
  EnumAction apply(ItemStack arg0);
}
