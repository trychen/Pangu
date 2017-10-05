package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

@FunctionalInterface
public interface ItemRightClickCallback {
    /**
     * Callback version of {@link net.minecraft.item.Item#onItemRightClick(World, EntityPlayer, EnumHand)}
     */
    ActionResult<ItemStack> apply(World worldIn, EntityPlayer playerIn, EnumHand handIn);
}
