package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemOnDroppedByPlayerCallback {
  boolean apply(ItemStack arg0, EntityPlayer arg1);
}
