package cn.mccraft.pangu.core.client.gui;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Style {
    private final ResourceLocation texture;
    private final int x, y, width, height, textOffset, fontColor, hoverFontColor, disabledFontColor;
    private final boolean fontShadow;

    protected Style(ResourceLocation texture, int x, int y, int width, int height, int textOffset, int fontColor, int hoverColor, int disabledColor, boolean fontShadow) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textOffset = textOffset;
        this.fontColor = fontColor;
        this.hoverFontColor = hoverColor;
        this.disabledFontColor = disabledColor;
        this.fontShadow = fontShadow;
    }

    public static Style of(ResourceLocation texture, int x, int y, int width, int height, int textOffset, int fontColor, int hoverColor, int disabledColor, boolean fontShadow) {
        return new Style(texture, x, y, width, height, textOffset, fontColor, hoverColor, disabledColor, fontShadow);
    }

    public static Style of(ResourceLocation texture, int x, int y, int width, int height) {
        return new Style(texture, x, y, width, height, 0, 0, 0, 0, false);
    }

    public static Style of(ResourceLocation texture, int x, int y, int size) {
        return of(texture, x, y, size, size);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTextOffset() {
        return textOffset;
    }

    public int getFontColor() {
        return fontColor;
    }

    public int getHoverFontColor() {
        return hoverFontColor;
    }

    public int getDisabledFontColor() {
        return disabledFontColor;
    }

    public boolean hasFontShadow() {
        return fontShadow;
    }
}
