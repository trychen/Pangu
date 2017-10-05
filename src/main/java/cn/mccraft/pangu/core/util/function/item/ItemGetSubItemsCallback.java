package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ItemGetSubItemsCallback extends BiConsumer<CreativeTabs, NonNullList<ItemStack>> {
    default void apply(CreativeTabs tab, NonNullList<ItemStack> items) {
        accept(tab, items);
    }
}
