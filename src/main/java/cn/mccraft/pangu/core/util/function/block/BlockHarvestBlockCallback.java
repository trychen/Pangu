package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockHarvestBlockCallback {
  void apply(World arg0, EntityPlayer arg1, BlockPos arg2, IBlockState arg3, TileEntity arg4,
      ItemStack arg5);
}
