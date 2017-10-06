package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockRemovedByPlayerCallback {
  boolean apply(IBlockState arg0, World arg1, BlockPos arg2, EntityPlayer arg3, boolean arg4);
}
