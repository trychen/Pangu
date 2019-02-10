package cn.mccraft.pangu.core.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum DefaultFontProvider implements FontProvider {
    INSTANCE;

    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    @Override
    public int getStringWidth(String text) {
        return fontRenderer.getStringWidth(text);
    }

    @Override
    public int drawString(String text, float x, float y, int color, boolean shadow) {
        return fontRenderer.drawString(text, x, y, color, shadow);
    }
}
