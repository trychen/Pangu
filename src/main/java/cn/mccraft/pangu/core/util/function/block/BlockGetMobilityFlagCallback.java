package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;

@FunctionalInterface
public interface BlockGetMobilityFlagCallback {
  EnumPushReaction apply(IBlockState arg0);
}
