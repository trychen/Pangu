package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockCreateTileEntityCallback {
  TileEntity apply(World arg0, IBlockState arg1);
}
