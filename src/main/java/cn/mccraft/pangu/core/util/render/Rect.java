package cn.mccraft.pangu.core.util.render;

import cn.mccraft.pangu.core.util.image.TextureProvider;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.util.render.RenderUtils.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

@SuppressWarnings("Duplicates")
@SideOnly(Side.CLIENT)
public interface Rect {
    static void startDrawing() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    static void endDrawing() {
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
    }

    static void bind(ResourceLocation resourceLocation) {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }

    static void bind(TextureProvider textureProvider) {
        ResourceLocation texture = textureProvider.getTexture();
        if (texture != null) bind(texture);
    }

    static void textureFiltering() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

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
    static void drawGradient(float left, float top, float right, float bottom, int colorLeftTop, int colorRightTop, int colorLeftBottom, int colorRightBottom) {
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
        GlStateManager.enableTexture2D();
    }

    static void drawGradientBox(float x, float y, float width, float height, int colorLeftTop, int colorRightTop, int colorLeftBottom, int colorRightBottom) {
        drawGradient(x, y, x + width, y + height, colorLeftTop, colorRightTop, colorLeftBottom, colorRightBottom);
    }

    static void drawGradientTop2Bottom(float left, float top, float right, float bottom, int colorTop, int colorBottom) {
        Rect.drawGradient(left, top, right, bottom, colorTop, colorTop, colorBottom, colorBottom);
    }

    static void drawGradientLeft2Right(float left, float top, float right, float bottom, int colorLeft, int colorRight) {
        Rect.drawGradient(left, top, right, bottom, colorLeft, colorRight, colorLeft, colorRight);
    }

    /**
     * Draws a rect with TextureManager#bindTexture(ResourceLocation).
     */
    static void drawTextured(float x, float y, float u, float v, float width, float height) {
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
    static void drawTextured(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
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
    static void drawBox(float x, float y, float width, float height, int color) {
        draw(x, y, x + width, y + height, color);
    }

    static void drawFrame(float left, float top, float right, float bottom, float border, int color) {
        draw(left, top, left + border, bottom, color);
        draw(right - border, top, right, bottom, color);
        draw(left + border, top, right - border, top + border, color);
        draw(left + border, bottom - border, right - border, bottom, color);
    }

    static void drawFrameBox(float x, float y, float width, float height, float border, int color) {
        drawFrame(x, y, x + width, y + height, border, color);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     */
    static void draw(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }

        float r = red(color);
        float b = blue(color);
        float g = green(color);
        float a = alpha(color);

        GlStateManager.color(r, b, g, a);
        GlStateManager.disableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
    }

    static void drawCustomSizeTextured(float x, float y, float width, float height) {
        Rect.drawCustomSizeTextured(x, y, 0, 0, width, height, width, height);
    }

    static void drawCustomSizeTextured(float x, float y, float width, float height, float factor) {
        Rect.drawCustomSizeTextured(x, y, 0, 0, width, height, width * factor, height * factor);
    }
    
    static void drawCustomSizeTextured(float x, float y, float u, float v, float width, float height, float factor) {
        Rect.drawCustomSizeTextured(x, y, u, v, width, height, width * factor, height * factor); 
    }

    static void drawCustomSizeTextured(float x, float y, float uWidth, float vHeight, float width, float height) {
        Rect.drawCustomSizeTextured(x, y, 0, 0, uWidth, vHeight, width, height);
    }

    static void drawCustomSizeTextured(float x, float y, float u, float v, float uWidth, float vHeight, float width, float height) {
        drawCustomSizeTextured(x, y, u, v, uWidth, vHeight, uWidth, vHeight, width, height);
    }

    static void drawCustomSizeTextured(float x, float y, float u, float v, float uWidth, float vHeight, float textureWidth, float textureHeight, float width, float height) {
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
        bufferbuilder.pos(x + width, y, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
        bufferbuilder.pos(x, y, 0.0D).tex(u * f, (v * f1)).endVertex();
        tessellator.draw();
    }

    static void drawCorner(float x, float y, float width, float height, float u, float v, float uSize, float vSize) {
        float halfWidth = width / 2, halfHeight = height / 2;

        drawTextured(x, y, u, v, halfWidth, halfHeight);
        drawTextured(x + halfWidth, y, u + uSize - halfWidth, v, halfWidth, halfHeight);
        drawTextured(x, y + halfHeight, u, v + vSize - halfHeight, halfWidth, halfHeight);
        drawTextured(x + halfWidth, y + halfHeight, u + uSize - halfWidth, v + vSize - halfHeight, halfWidth, halfHeight);
    }

    ResourceLocation CLASSICAL_BACKGROUND = PanguResLoc.ofGui("classical_background.png");

    static void drawClassicalBackground(float x, float y, float width, float height) {
        startDrawing();
        bind(CLASSICAL_BACKGROUND);
        drawCorner(x, y, width, height, 0, 0, 256, 256);
    }
}
