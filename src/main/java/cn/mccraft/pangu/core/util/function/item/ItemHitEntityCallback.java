package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemHitEntityCallback {
    boolean apply(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker);
}
