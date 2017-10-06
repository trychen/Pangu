package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemOnItemUseFinishCallback {
  ItemStack apply(ItemStack arg0, World arg1, EntityLivingBase arg2);
}
