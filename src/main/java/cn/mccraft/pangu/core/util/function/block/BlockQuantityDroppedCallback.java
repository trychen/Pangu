package cn.mccraft.pangu.core.util.function.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockQuantityDroppedCallback {
  int apply(IBlockState arg0, int arg1, Random arg2);
}
