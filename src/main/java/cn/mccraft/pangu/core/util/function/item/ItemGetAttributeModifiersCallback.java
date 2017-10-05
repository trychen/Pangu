package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface ItemGetAttributeModifiersCallback extends BiPredicate<ItemStack, ItemStack> {
    default boolean apply(ItemStack toRepair, ItemStack repair) {
        return test(toRepair, repair);
    }
}
