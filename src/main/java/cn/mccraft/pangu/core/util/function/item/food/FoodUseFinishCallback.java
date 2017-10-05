package cn.mccraft.pangu.core.util.function.item.food;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface FoodUseFinishCallback {
    FoodUseFinishCallback BASIC_CALLBACK = (stack, worldIn, entityLiving) -> {};
    void apply(ItemStack stack, World worldIn, EntityLivingBase entityLiving);
}
