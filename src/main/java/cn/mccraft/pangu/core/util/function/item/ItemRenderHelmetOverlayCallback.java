package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface ItemRenderHelmetOverlayCallback {
  void apply(ItemStack arg0, EntityPlayer arg1, ScaledResolution arg2, float arg3);
}
