package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockIsToolEffectiveCallback {
  boolean apply(String arg0, IBlockState arg1);
}
