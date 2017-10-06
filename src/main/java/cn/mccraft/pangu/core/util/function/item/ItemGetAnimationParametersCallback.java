package cn.mccraft.pangu.core.util.function.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemGetAnimationParametersCallback {
  ImmutableMap apply(ItemStack arg0, World arg1, EntityLivingBase arg2);
}
