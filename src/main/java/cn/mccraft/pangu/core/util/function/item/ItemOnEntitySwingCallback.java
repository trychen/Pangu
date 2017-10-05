package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface ItemOnEntitySwingCallback extends BiPredicate<EntityLivingBase, ItemStack> {
    default boolean apply(EntityLivingBase entityLiving, ItemStack stack) {
        return test(entityLiving, stack);
    }
}
