package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemOnArmorTickCallback {
    void apply(World world, EntityPlayer player, ItemStack itemStack);
}
