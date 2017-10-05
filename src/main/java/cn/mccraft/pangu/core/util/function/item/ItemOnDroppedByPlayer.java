package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.function.BiPredicate;

@FunctionalInterface
public interface ItemOnDroppedByPlayer extends BiPredicate<ItemStack, EntityPlayer> {
    default boolean apply(ItemStack item, EntityPlayer player) {
        return test(item, player);
    }
}
