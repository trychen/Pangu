package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@FunctionalInterface
public interface ItemInitCapabilitiesCallback {
  ICapabilityProvider apply(ItemStack arg0, NBTTagCompound arg1);
}
