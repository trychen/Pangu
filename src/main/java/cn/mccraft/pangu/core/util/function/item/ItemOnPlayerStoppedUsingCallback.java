package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemOnPlayerStoppedUsingCallback {
    void apply(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft);
}
