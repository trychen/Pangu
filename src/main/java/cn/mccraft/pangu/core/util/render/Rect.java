package cn.mccraft.pangu.core.util.render;

import cn.mccraft.pangu.core.util.image.TextureProvider;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("Duplicates")
@SideOnly(Side.CLIENT)
public interface Rect {
    int[] ZLEVEL = {0};
    ResourceLocation CLASSICAL_BACKGROUND = PanguResLoc.ofGui("classical_background.png");
    Random RAND = new Random();

    static void startDrawing() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableAlpha();
        Rect.color();
    }

    static void endDrawing() {
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.disableTexture2D();
    }

    static void bind(ResourceLocation resourceLocation) {
        if (resourceLocation != null) Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }

    static void bind(TextureProvider textureProvider) {
        if (textureProvider == null) {
            GlStateManager.bindTexture(TextureUtil.MISSING_TEXTURE.getGlTextureId());
            return;
        }
        textureProvider.bind();
    }

    static void bind(TextureProvider textureProvider, ResourceLocation defaultTexture) {
        if (textureProvider == null) {
            bind(defaultTexture);
            return;
        }

        textureProvider.bind(defaultTexture);
    }

    static void bind(ResourceLocation texture, ResourceLocation defaultTexture) {
        if (texture == null) {
            bind(defaultTexture);
            return;
        }
        bind(texture);
    }

    static void bindWithFiltering(ResourceLocation resourceLocation) {
        bind(resourceLocation);
        linearFiltering();
    }

    static void bindWithFiltering(TextureProvider textureProvider) {
        bind(textureProvider);
        linearFiltering();
    }

    static void zLevel() {
        ZLEVEL[0] = 0;
    }

    static void zLevel(int level) {
        ZLEVEL[0] = level;
    }

    static void color(int color) {
        GlStateManager.color(RenderUtils.red(color), RenderUtils.green(color), RenderUtils.blue(color), RenderUtils.alpha(color));
    }

    static void color() {
        color(1F, 1F, 1F, 1F);
    }

    static void color(float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
    }

    static void color(float r, float g, float b) {
        GlStateManager.color(r, g, b);
    }

    static void color(int r, int g, int b, int a) {
        GlStateManager.color(r, g, b, a);
    }

    @Deprecated
    static void textureFiltering() {
        linearFiltering();
    }

    static void resetFiltering() {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
    }

    static void nearestFiltering() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    static void linearFiltering() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    static void normalFiltering() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    static void linearMipmapLinearFiltering() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
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
        int zLevel = ZLEVEL[0];
        bufferbuilder
                .pos(right, top, zLevel)
                .color(red(colorRightTop), green(colorRightTop), blue(colorRightTop), alpha(colorRightTop))
                .endVertex();

        bufferbuilder
                .pos(left, top, zLevel)
                .color(red(colorLeftTop), green(colorLeftTop), blue(colorLeftTop), alpha(colorLeftTop))
                .endVertex();

        bufferbuilder
                .pos(left, bottom, zLevel)
                .color(red(colorLeftBottom), green(colorLeftBottom), blue(colorLeftBottom), alpha(colorLeftBottom))
                .endVertex();

        bufferbuilder
                .pos(right, bottom, zLevel)
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
        int zLevel = ZLEVEL[0];
        buffer
                .pos(x, y + height, zLevel)
                .tex(u * 0.00390625F, (v + height) * 0.00390625F)
                .endVertex();
        buffer
                .pos(x + width, y + height, zLevel)
                .tex((u + width) * 0.00390625F, (v + height) * 0.00390625F)
                .endVertex();
        buffer
                .pos(x + width, y, zLevel)
                .tex((u + width) * 0.00390625F, v * 0.00390625F)
                .endVertex();
        buffer
                .pos(x, y, zLevel)
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

        int zLevel = ZLEVEL[0];

        buffer
                .pos(x, y + height, zLevel)
                .tex(u * f, (v + height) * f1)
                .endVertex();
        buffer
                .pos(x + width, y + height, zLevel)
                .tex((u + width) * f, (v + height) * f1)
                .endVertex();
        buffer
                .pos(x + width, y, zLevel)
                .tex((u + width) * f, v * f1)
                .endVertex();
        buffer
                .pos(x, y, zLevel)
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

    static void drawCenterBoxInCenter(float x, float y, float width, float height, int color) {
        drawBox(x - width / 2F, y - height / 2F, width, height, color);
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

        color(color);
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        int zLevel = ZLEVEL[0];

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, zLevel).endVertex();
        bufferbuilder.pos(right, bottom, zLevel).endVertex();
        bufferbuilder.pos(right, top, zLevel).endVertex();
        bufferbuilder.pos(left, top, zLevel).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
    }

    static void drawFullTexTextured(float x, float y, float width, float height) {
        int zLevel = ZLEVEL[0];
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(1, 0).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    static void drawFullTexTexturedInCenter(float x, float y, float width, float height) {
        drawFullTexTextured(x - width * 0.5F, y - height * 0.5F, width, height);
    }

    static void drawFullTexTextured(float x, float y, float width, float height, float factor) {
        drawFullTexTextured(x, y, width * factor, height * factor);
    }

    static void drawFullTexTexturedInCenter(float x, float y, float width, float height, float factor) {
        drawFullTexTextured(x - width * factor * 0.5F, y - height * factor * 0.5F, width * factor, height * factor);
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
        int zLevel = ZLEVEL[0];
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(u * f, (v + vHeight) * f1).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex((u + uWidth) * f, v * f1).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(u * f, (v * f1)).endVertex();
        tessellator.draw();
    }

    static void drawFlexibleCorner(float x, float y, float width, float height, float textureWidth, float textureHeight, float top, float bottom, float left, float right) {
        float horizontalSpacing = width - left - right;
        float verticalSpacing = height - top - bottom;

        if (horizontalSpacing < 0) horizontalSpacing = 0;
        if (verticalSpacing < 0) verticalSpacing = 0;

        float perWidth = 1.0F / textureWidth;
        float perHeight = 1.0F / textureHeight;

        int zLevel = ZLEVEL[0];

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(1, 0).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    static void drawCorner(float x, float y, float width, float height, float u, float v, float uSize, float vSize) {
        float halfWidth = width / 2, halfHeight = height / 2;

        drawTextured(x, y, u, v, halfWidth, halfHeight);
        drawTextured(x + halfWidth, y, u + uSize - halfWidth, v, halfWidth, halfHeight);
        drawTextured(x, y + halfHeight, u, v + vSize - halfHeight, halfWidth, halfHeight);
        drawTextured(x + halfWidth, y + halfHeight, u + uSize - halfWidth, v + vSize - halfHeight, halfWidth, halfHeight);
    }

    static void drawClassicalBackground(float x, float y, float width, float height) {
        startDrawing();
        bind(CLASSICAL_BACKGROUND);
        drawCorner(x, y, width, height, 0, 0, 256, 256);
    }

    static void drawRegularPolygon(double x, double y, int radius, int sides) {
        int zLevel = Rect.ZLEVEL[0];
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator instance = Tessellator.getInstance();
        BufferBuilder buffer = instance.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        buffer.pos(x, y, zLevel).endVertex();

        double pi = Math.PI * 2;

        for (int i = 0; i <= sides; i++) {
            double angle = (pi * i / sides) + Math.toRadians(180);
            buffer.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, zLevel).endVertex();
        }
        instance.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    static void drawFullTexTexturedVerticalProgress(float x, float y, float width, float height, float progress) {
        int zLevel = Rect.ZLEVEL[0];

        y += height * (1 - progress);
        height *= progress;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(1, 1).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(1, 1 - progress).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(0, 1 - progress).endVertex();
        tessellator.draw();
    }

    static void drawFullTexTexturedVerticalProgress(float x, float y, float width, float height, float factor, float progress) {
        drawFullTexTexturedVerticalProgress(x, y, width * factor, height * factor, progress);
    }

    static void drawFullTexTexturedProgress(float x, float y, float width, float height, float progress) {
        int zLevel = Rect.ZLEVEL[0];
        width *= progress;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(0, 1).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(1 * progress, 1).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(1 * progress, 0).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(0, 0).endVertex();
        tessellator.draw();
    }

    static void drawFullTexTexturedProgress(float x, float y, float width, float height, float factor, float progress) {
        drawFullTexTexturedProgress(x, y, width * factor, height * factor, progress);
    }

    /**
     * Draws a texture rectangle using the texture currently bound to the TextureManager
     */
    static void drawTextureAtlasSprite(float x, float y, TextureAtlasSprite textureSprite, float width, float height) {
        int zLevel = Rect.ZLEVEL[0];
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
        bufferbuilder.pos(x + width, y + height, zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
        bufferbuilder.pos(x + width, y, zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
        bufferbuilder.pos(x, y, zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }

    static float alpha(int color) {
        return (color >>> 24 & 0xFF) / 255.0F;
    }

    static float red(int color) {
        return (color >>> 16 & 0xFF) / 255.0F;
    }

    static float green(int color) {
        return (color >>> 8 & 0xFF) / 255.0F;
    }

    static float blue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    static int alphaInt(int color) {
        return color >>> 24 & 255;
    }

    static int redInt(int color) {
        return color >> 16 & 0xFF;
    }

    static int greenInt(int color) {
        return color >> 8 & 0xFF;
    }

    static int blueInt(int color) {
        return color & 0xFF;
    }

    static int alpha(int color, float alpha) {
        return alpha(color, (int) (alpha * 0xFF));
    }

    static int alpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    static int red(int color, float red) {
        return red(color, (int) (red * 0xFF));
    }

    static int red(int color, int red) {
        return (color & 0xFF00FFFF) | (red << 16);
    }

    static int green(int color, float green) {
        return green(color, (int) (green * 0xFF));
    }

    static int green(int color, int green) {
        return (color & 0xFFFF00FF) | (green << 8);
    }

    static int blue(int color, float blue) {
        return blue(color, (int) (blue * 0xFF));
    }

    static int blue(int color, int blue) {
        return (color & 0xFFFFFF00) | blue;
    }

    static int randomColor() {
        return (RAND.nextInt() << 8 >>> 8) | 0xFF000000;
    }
}
