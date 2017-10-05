package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemBlockDestroyedCallback {
    /**
     * Callback version of {@link net.minecraft.item.Item#onBlockDestroyed(ItemStack, World, IBlockState, BlockPos, EntityLivingBase)}
     */
    boolean apply(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving);
}
