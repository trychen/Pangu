package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

@FunctionalInterface
public interface ItemItemInteractionForEntityCallback {
  boolean apply(ItemStack arg0, EntityPlayer arg1, EntityLivingBase arg2, EnumHand arg3);
}
