package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemIsValidArmorCallback {
    boolean apply(ItemStack stack, EntityEquipmentSlot armorType, Entity entity);
}
