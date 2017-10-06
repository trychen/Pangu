package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockGetMaterialCallback {
  Material apply(IBlockState arg0);
}
