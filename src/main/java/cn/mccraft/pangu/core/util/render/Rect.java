package cn.mccraft.pangu.core.util.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.util.render.RenderUtils.*;

@SuppressWarnings("Duplicates")
@SideOnly(Side.CLIENT)
public interface Rect {
    /**
     * draw a gradient color rect
     *
     * @param left             x1
     * @param top              y2
     * @param right            x2
     * @param bottom           y2
     * @param colorLeftTop     the color of (x1, y1)
     * @param colorRightTop    the color of (x2, y1)
     * @param colorLeftBottom  the color of (x1, y2)
     * @param colorRightBottom the color of (x2, y2)
     */
    static void drawGradient(double left, double top, double right, double bottom, int colorLeftTop, int colorRightTop, int colorLeftBottom, int colorRightBottom) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder
                .pos(right, top, 0)
                .color(red(colorRightTop), green(colorRightTop), blue(colorRightTop), alpha(colorRightTop))
                .endVertex();

        bufferbuilder
                .pos(left, top, 0)
                .color(red(colorLeftTop), green(colorLeftTop), blue(colorLeftTop), alpha(colorLeftTop))
                .endVertex();

        bufferbuilder
                .pos(left, bottom, 0)
                .color(red(colorLeftBottom), green(colorLeftBottom), blue(colorLeftBottom), alpha(colorLeftBottom))
                .endVertex();

        bufferbuilder
                .pos(right, bottom, 0)
                .color(red(colorRightBottom), green(colorRightBottom), blue(colorRightBottom), alpha(colorRightBottom))
                .endVertex();

        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Draws a rect with TextureManager#bindTexture(ResourceLocation).
     */
    static void drawTextured(double x, double y, float u, float v, float width, float height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        buffer
                .pos(x, y + height, 0)
                .tex(u * 0.00390625F, (v + height) * 0.00390625F)
                .endVertex();
        buffer
                .pos(x + width, y + height, 0)
                .tex((u + width) * 0.00390625F, (v + height) * 0.00390625F)
                .endVertex();
        buffer
                .pos(x + width, y, 0)
                .tex((u + width) * 0.00390625F, v * 0.00390625F)
                .endVertex();
        buffer
                .pos(x, y, 0)
                .tex(u * 0.00390625F, v * 0.00390625F)
                .endVertex();
        tessellator.draw();
    }

    /**
     * Draws a rect with custom size texture.
     */
    static void drawTextured(double x, double y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;

        buffer
                .pos(x, y + height, 0)
                .tex(u * f, (v + height) * f1)
                .endVertex();
        buffer
                .pos(x + width, y + height, 0)
                .tex((u + width) * f, (v + height) * f1)
                .endVertex();
        buffer
                .pos(x + width, y, 0)
                .tex((u + width) * f, v * f1)
                .endVertex();
        buffer
                .pos(x, y, 0)
                .tex(u * f, v * f1)
                .endVertex();
        tessellator.draw();
    }


    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     */
    static void drawBox(double left, double top, double width, double height, int color) {
        float a = alpha(color);
        float r = red(color);
        float g = green(color);
        float b = blue(color);

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, a);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, top + height, 0.0D).endVertex();
        bufferbuilder.pos(left + width, top + height, 0.0D).endVertex();
        bufferbuilder.pos(left + width, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     */
    static void draw(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float a = alpha(color);
        float r = RenderUtils.red(color);
        float g = RenderUtils.green(color);
        float b = RenderUtils.blue(color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(r, g, b, a);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    static void drawCustomSizeTextured(float x, float y, int uWidth, int vHeight, int width, int height) {
        Rect.drawCustomSizeTextured(x, y, 0, 0, uWidth, vHeight, width, height);
    }

    static void drawCustomSizeTextured(float x, float y, float u, float v, int uWidth, int vHeight, int width, int height) {
        float f = 1.0F / uWidth;
        float f1 = 1.0F / vHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0D).tex((double) (u * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).tex((double) ((u + (float) uWidth) * f), (double) ((v + (float) vHeight) * f1)).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).tex((double) ((u + (float) uWidth) * f), (double) (v * f1)).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        tessellator.draw();
    }
}
