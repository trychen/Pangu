package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemOnBlockDestroyedCallback {
  boolean apply(ItemStack arg0, World arg1, IBlockState arg2, BlockPos arg3, EntityLivingBase arg4);
}
