package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ItemGetHighlightTipCallback extends BiFunction<ItemStack, String, String> {
}
