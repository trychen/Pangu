package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetDestroySpeedCallback {
  float apply(ItemStack arg0, IBlockState arg1);
}
