package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockBeginLeavesDecayCallback {
  void apply(IBlockState arg0, World arg1, BlockPos arg2);
}
