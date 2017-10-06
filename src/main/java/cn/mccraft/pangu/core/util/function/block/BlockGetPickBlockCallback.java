package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockGetPickBlockCallback {
  ItemStack apply(IBlockState arg0, RayTraceResult arg1, World arg2, BlockPos arg3,
      EntityPlayer arg4);
}
