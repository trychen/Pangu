package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface ItemDoesSneakBypassUseCallback {
    boolean apply(ItemStack stack, net.minecraft.world.IBlockAccess world, BlockPos pos, EntityPlayer player);
}
