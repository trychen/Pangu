package cn.mccraft.pangu.core.capability;

import net.minecraft.block.state.IBlockState;

public interface ItemStats {
    float getDestroySpeed(IBlockState state);
}
