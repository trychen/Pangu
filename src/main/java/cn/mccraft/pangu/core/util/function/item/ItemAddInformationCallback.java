package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@FunctionalInterface
public interface ItemAddInformationCallback {
    void apply(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn);
}
