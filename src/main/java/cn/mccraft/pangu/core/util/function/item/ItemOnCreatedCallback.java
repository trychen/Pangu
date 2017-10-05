package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemOnCreatedCallback {
    void apply(ItemStack stack, World worldIn, EntityPlayer playerIn);
}
