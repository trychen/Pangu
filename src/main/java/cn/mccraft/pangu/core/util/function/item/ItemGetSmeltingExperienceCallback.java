package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetSmeltingExperienceCallback {
  float apply(ItemStack arg0);
}
