package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockGetPlayerRelativeBlockHardnessCallback {
  float apply(IBlockState arg0, EntityPlayer arg1, World arg2, BlockPos arg3);
}
