package cn.mccraft.pangu.core.util.function.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockRandomTickCallback {
  void apply(World arg0, BlockPos arg1, IBlockState arg2, Random arg3);
}
