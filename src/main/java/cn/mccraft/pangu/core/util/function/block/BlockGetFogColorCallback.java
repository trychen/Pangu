package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockGetFogColorCallback {
  Vec3d apply(World arg0, BlockPos arg1, IBlockState arg2, Entity arg3, Vec3d arg4, float arg5);
}
