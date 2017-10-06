package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

@FunctionalInterface
public interface BlockCanRenderInLayerCallback {
  boolean apply(IBlockState arg0, BlockRenderLayer arg1);
}
