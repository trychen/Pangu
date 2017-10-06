package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemCanDestroyBlockInCreativeCallback {
  boolean apply(World arg0, BlockPos arg1, ItemStack arg2, EntityPlayer arg3);
}
