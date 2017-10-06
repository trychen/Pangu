package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@FunctionalInterface
public interface BlockGetBlockFaceShapeCallback {
  BlockFaceShape apply(IBlockAccess arg0, IBlockState arg1, BlockPos arg2, EnumFacing arg3);
}
