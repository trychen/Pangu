package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockSetHarvestLevelCallback {
  void apply(String arg0, int arg1, IBlockState arg2);
}
