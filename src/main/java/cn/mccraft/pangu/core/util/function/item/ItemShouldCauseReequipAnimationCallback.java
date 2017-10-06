package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemShouldCauseReequipAnimationCallback {
  boolean apply(ItemStack arg0, ItemStack arg1, boolean arg2);
}
