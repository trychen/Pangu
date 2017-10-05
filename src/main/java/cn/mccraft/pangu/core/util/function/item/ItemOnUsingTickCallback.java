package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemOnUsingTickCallback {
    void apply(ItemStack stack, EntityLivingBase player, int count);
}
