package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetRarityCallback {
  EnumRarity apply(ItemStack arg0);
}
