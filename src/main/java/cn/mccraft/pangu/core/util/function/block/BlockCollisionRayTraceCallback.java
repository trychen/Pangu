package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockCollisionRayTraceCallback {
  RayTraceResult apply(IBlockState arg0, World arg1, BlockPos arg2, Vec3d arg3, Vec3d arg4);
}
