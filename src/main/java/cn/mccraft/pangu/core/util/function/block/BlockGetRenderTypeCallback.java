package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

@FunctionalInterface
public interface BlockGetRenderTypeCallback {
  EnumBlockRenderType apply(IBlockState arg0);
}
