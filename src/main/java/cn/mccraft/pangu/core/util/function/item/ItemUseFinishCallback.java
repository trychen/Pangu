package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemUseFinishCallback {
    /**
     * Callback version of {@link net.minecraft.item.Item#onItemUseFinish(ItemStack, World, EntityLivingBase)}
     */
    ItemStack apply(ItemStack stack, World worldIn, EntityLivingBase entityLiving);
}
