package cn.mccraft.pangu.core.util.function.item;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.util.function.Function;

@FunctionalInterface
public interface ItemGetFontRendererCallback extends Function<ItemStack, FontRenderer> {
}
