package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Function;

@FunctionalInterface
public interface ItemGetNBTShareTagCallback extends Function<ItemStack, NBTTagCompound> {
}
