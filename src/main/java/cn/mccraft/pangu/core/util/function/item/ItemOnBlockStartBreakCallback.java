package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface ItemOnBlockStartBreakCallback {
    boolean apply(ItemStack itemstack, BlockPos pos, EntityPlayer player);
}
