package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemOnUpdateCallback {
    void apply(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected);
}
