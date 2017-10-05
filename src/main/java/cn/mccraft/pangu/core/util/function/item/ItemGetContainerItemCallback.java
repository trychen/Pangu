package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

import java.util.function.Function;

@FunctionalInterface
public interface ItemGetContainerItemCallback extends Function<ItemStack, ItemStack> {
}
