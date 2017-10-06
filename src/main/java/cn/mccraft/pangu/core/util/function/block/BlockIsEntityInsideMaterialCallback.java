package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

@FunctionalInterface
public interface BlockIsEntityInsideMaterialCallback {
  Boolean apply(IBlockAccess arg0, BlockPos arg1, IBlockState arg2, Entity arg3, double arg4,
      Material arg5, boolean arg6);
}
