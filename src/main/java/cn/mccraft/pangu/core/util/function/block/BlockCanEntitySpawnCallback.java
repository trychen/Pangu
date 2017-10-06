package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

@FunctionalInterface
public interface BlockCanEntitySpawnCallback {
  boolean apply(IBlockState arg0, Entity arg1);
}
