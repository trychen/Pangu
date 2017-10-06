package cn.mccraft.pangu.core.util.function.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface BlockOnBlockActivatedCallback {
  boolean apply(World arg0, BlockPos arg1, IBlockState arg2, EntityPlayer arg3, EnumHand arg4,
      EnumFacing arg5, float arg6, float arg7, float arg8);
}
