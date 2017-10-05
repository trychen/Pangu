package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemCreateEntityCallback {
    Entity apply(World world, Entity location, ItemStack itemstack);
}
