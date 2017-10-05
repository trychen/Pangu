package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

@FunctionalInterface
public interface ItemHashCustomEntityCallback extends Predicate<ItemStack> {
    default boolean apply(ItemStack stack) {
        return test(stack);
    }
}
