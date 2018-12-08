package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public final class Icon {
    private final ResourceLocation texture;
    private final int offsetX, offsetY, size;

    public Icon(ResourceLocation texture, int offsetX, int offsetY, int size) {
        this.texture = texture;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.size = size;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getSize() {
        return size;
    }

    public void draw(int x, int y) {
        draw(x, y, size);
    }

    public void draw(int x, int y, int boxSize) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());
        Rect.drawTextured(
                x + (float) (boxSize - getSize()) / 2, y + (float) (boxSize - getSize()) / 2,
                getOffsetX(), getOffsetY(),
                getSize(), getSize());

        GlStateManager.disableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
    }

    public static Icon of(ResourceLocation texture) {
        return new Icon(texture, 0, 0, 11);
    }

    public static Icon of(ResourceLocation texture, int size) {
        return new Icon(texture, 0, 0, size);
    }

    public static Icon of(ResourceLocation texture, int offsetX, int offsetY) {
        return new Icon(texture, offsetX, offsetY, 11);
    }

    public static Icon of(ResourceLocation texture, int offsetX, int offsetY, int size) {
        return new Icon(texture, offsetX, offsetY, size);
    }
}
