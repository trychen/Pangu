package cn.mccraft.pangu.core.util.function.item;

import java.util.Set;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetToolClassesCallback {
  Set apply(ItemStack arg0);
}
