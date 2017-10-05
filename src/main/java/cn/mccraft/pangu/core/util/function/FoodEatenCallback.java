package cn.mccraft.pangu.core.util.function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface FoodEatenCallback {
    FoodEatenCallback BASIC_CALLBACK = (stack, worldIn, player) -> {};
    void accept(ItemStack stack, World worldIn, @Nonnull EntityPlayer player);
}
