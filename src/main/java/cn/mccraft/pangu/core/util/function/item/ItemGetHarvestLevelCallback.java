package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetHarvestLevelCallback {
  int apply(ItemStack arg0, String arg1, EntityPlayer arg2, IBlockState arg3);
}
