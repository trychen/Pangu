package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@FunctionalInterface
public interface ItemInteractionForEntityCallback {
    boolean apply(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand);
}
