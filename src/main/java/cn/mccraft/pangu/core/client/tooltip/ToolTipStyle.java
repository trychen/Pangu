package cn.mccraft.pangu.core.client.tooltip;

import net.minecraft.util.ResourceLocation;
import static cn.mccraft.pangu.core.client.PGClient.PG_TOOLTIPS_TEXTURE;

public enum  ToolTipStyle {
    NONE((byte) 0, null, 0, 0, 0, 0, 0, 0xFFFFFF),
    NORMAL((byte) 1, PG_TOOLTIPS_TEXTURE, 0, 0, 200, 15, 3, 0xFFFFFF),
    TRANSPARENT((byte) 2, PG_TOOLTIPS_TEXTURE, 0, 15, 200, 15, 3, 0xFFFFFF);

    private final byte id;
    private final ResourceLocation texture;
    private final int x, y, width, height, textOffset, fontColor;

    ToolTipStyle(byte id, ResourceLocation texture, int x, int y, int width, int height, int textOffset, int fontColor) {
        this.id = id;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textOffset = textOffset;
        this.fontColor = fontColor;
    }

    public byte getId() {
        return this.id;
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

    public static ToolTipStyle valueOf(byte id) {
        for (ToolTipStyle value : values()) {
            if (value.id == id) return value;
        }
        return NORMAL;
    }
}
