package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockCanCollideCheckCallback {
  boolean apply(IBlockState arg0, boolean arg1);
}
