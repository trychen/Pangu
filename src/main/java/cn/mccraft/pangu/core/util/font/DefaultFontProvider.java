package cn.mccraft.pangu.core.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

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

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    @Override
    public int drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        float half = getStringWidth(text) / 2;
        return drawString(text, x - half, y, color, shadow);
    }
}
