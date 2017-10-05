package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ItemGetEntityLifespanCallback extends BiFunction<ItemStack, World, Integer> {
}
