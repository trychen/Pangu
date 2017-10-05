package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemGetArmorTextureCallback {
    String apply(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type);
}
