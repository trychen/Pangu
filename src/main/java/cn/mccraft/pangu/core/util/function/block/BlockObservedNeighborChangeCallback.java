package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockObservedNeighborChangeCallback {
  void apply(IBlockState arg0, World arg1, BlockPos arg2, Block arg3, BlockPos arg4);
}
