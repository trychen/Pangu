package cn.mccraft.pangu.core.util.render;

import net.minecraft.client.Minecraft;

/**
 * 自定义字体
 *
 * @since 1.2.1.1
 */
public class CustomFont {
    public int size;
    private FontCache fontCache;
    private int[] colorCode;
    private String fontName;

    public CustomFont(String fontName, int fontSize) {
        this.setFont(fontName, fontSize);
    }

    public CustomFont(String fontName) {
        this.setFont(fontName);
    }

    public void setFont(String fontName) {
        this.setFont(fontName, 18);
    }

    public void setFont(String fontName, int fontSize) {
        this.fontName = fontName;
        this.size = fontSize;
        this.colorCode = new int[32];

        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (Minecraft.getMinecraft().gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }

        this.fontCache = new FontCache(this.colorCode);
        this.fontCache.setDefaultFont(fontName, fontSize, true);
    }

    public String getFontName() {
        return this.fontName;
    }

    public void refresh() {
        this.setFont(fontName);
    }

    public int drawString(String str, float posX, float posY, int color, boolean shadow) {
        return this.fontCache.renderString(str, (int) posX, (int) posY, color, shadow);
    }

    public int drawStringWithShadow(String str, int posX, int posY, int color) {
        int sColor = (color & 16579836) >> 2 | color & -16777216;
        int var6 = this.drawString(str, posX + 0.5F, posY + 0.5F, sColor, true);
        var6 = Math.max(var6, this.drawString(str, posX, posY, color, false));
        return var6;
    }


    public void drawCenteredString(String text, float x, float y, int color, boolean shadow) {
        float half = getStringWidth(text) / 2;
        drawString(text, (x - half), y, color, shadow);
    }

    public int getStringWidth(String str) {
        return this.fontCache.getStringWidth(str);
    }

    public FontCache getFontCache() {
        return fontCache;
    }

    public int getSize() {
        return size;
    }

    public String stripColorCodes(String original) {
        String colorCode = "0123456789abcdefklmnor";
        for (int x = 0; x < colorCode.length(); x++)
            original = original.replaceAll("\247" + colorCode.indexOf(x), "");
        return original;
    }
}