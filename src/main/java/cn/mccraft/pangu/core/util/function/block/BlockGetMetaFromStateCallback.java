package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockGetMetaFromStateCallback {
  int apply(IBlockState arg0);
}
