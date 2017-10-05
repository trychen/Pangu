package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface ItemIsBookEnchantableCallback extends BiPredicate<ItemStack, ItemStack> {
    default boolean apply(ItemStack stack, ItemStack book) {
        return test(stack, book);
    }
}
