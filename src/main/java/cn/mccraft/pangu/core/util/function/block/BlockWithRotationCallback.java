package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;

@FunctionalInterface
public interface BlockWithRotationCallback {
  IBlockState apply(IBlockState arg0, Rotation arg1);
}
