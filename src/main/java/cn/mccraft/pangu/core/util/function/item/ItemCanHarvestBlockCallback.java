package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemCanHarvestBlockCallback {
  boolean apply(IBlockState arg0, ItemStack arg1);
}
