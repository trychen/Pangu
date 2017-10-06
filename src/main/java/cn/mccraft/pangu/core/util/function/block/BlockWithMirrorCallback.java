package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;

@FunctionalInterface
public interface BlockWithMirrorCallback {
  IBlockState apply(IBlockState arg0, Mirror arg1);
}
