package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockIsNormalCubeCallback {
  boolean apply(IBlockState arg0);
}
