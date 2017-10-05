package cn.mccraft.pangu.core.util.function;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface FoodUseFinishCallback {
    FoodUseFinishCallback BASIC_CALLBACK = (stack, worldIn, entityLiving) -> {};
    void accept(ItemStack stack, World worldIn, EntityLivingBase entityLiving);
}
