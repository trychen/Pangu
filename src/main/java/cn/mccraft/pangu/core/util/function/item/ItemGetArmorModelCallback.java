package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetArmorModelCallback {
    net.minecraft.client.model.ModelBiped apply(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default);
}
