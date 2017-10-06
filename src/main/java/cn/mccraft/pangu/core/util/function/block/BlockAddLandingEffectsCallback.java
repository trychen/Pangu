package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

@FunctionalInterface
public interface BlockAddLandingEffectsCallback {
  boolean apply(IBlockState arg0, WorldServer arg1, BlockPos arg2, IBlockState arg3,
      EntityLivingBase arg4, int arg5);
}
