package cn.mccraft.pangu.core.util.function.block;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockAddCollisionBoxToListCallback {
  void apply(IBlockState arg0, World arg1, BlockPos arg2, AxisAlignedBB arg3, List arg4,
      Entity arg5, boolean arg6);
}
