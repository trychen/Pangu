package cn.mccraft.pangu.core.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class OpenGL {
    public static ArrayList<Framebuffer> frameBuffers = new ArrayList<Framebuffer>();
    public static boolean lightmapTexUnitTextureEnable;
    public static int lightmapTexUnit = OpenGlHelper.lightmapTexUnit;
    public static int defaultTexUnit = OpenGlHelper.defaultTexUnit;

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void translate(double offsetX, double offsetY, double offsetZ) {
        GL11.glTranslated(offsetX, offsetY, offsetZ);
    }

    public static void translate(float offsetX, float offsetY, float offsetZ) {
        GL11.glTranslatef(offsetX, offsetY, offsetZ);
    }

    public static void scale(double scaleX, double scaleY, double scaleZ) {
        GL11.glScaled(scaleX, scaleY, scaleZ);
    }

    public static void scale(float scaleX, float scaleY, float scaleZ) {
        GL11.glScalef(scaleX, scaleY, scaleZ);
    }

    public static void begin(int mode) {
        GL11.glBegin(mode);
    }

    public static void end() {
        GL11.glEnd();
    }

    public static void newList(int list, int mode) {
        GL11.glNewList(list, mode);
    }

    public static void callList(int list) {
        GL11.glCallList(list);
    }

    public static void endList() {
        GL11.glEndList();
    }

    public static void enableTexture2d() {
        enable(GL11.GL_TEXTURE_2D);
    }

    public static void disableTexture2d() {
        disable(GL11.GL_TEXTURE_2D);
    }

    public static void normal(float x, float y, float z) {
        GL11.glNormal3f(x, y, z);
    }

    public static void texCoord(float u, float v) {
        GL11.glTexCoord2f(u, v);
    }

    public static void vertex(float x, float y, float z) {
        GL11.glVertex3f(x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z) {
        GL11.glRotatef(angle, x, y, z);
    }

    public static void enableBlend() {
        enable(GL11.GL_BLEND);
    }

    public static void disableBlend() {
        disable(GL11.GL_BLEND);
    }

    public static void color(float r, float g, float b) {
        GL11.glColor3f(r, g, b);
    }

    public static void color(float r, float g, float b, float a) {
        GL11.glColor4f(r, g, b, a);
    }

    /**
     * Same functionality as GlStateManager.color
     *
     * @param color - Hexadecimal color value
     */
    public static void color4i(int color) {
        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        color(r, g, b, a);
    }

    /**
     * Same functionality as glColor3f
     *
     * @param color - Hexadecimal color value
     */
    public static void color3i(int color) {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        color(r, g, b);
    }

    public static String getString(int name) {
        return GL11.glGetString(name);
    }

    public static void enableDepthTest() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public static void disableDepthTest() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static void enable(int cap) {
        GL11.glEnable(cap);
    }

    public static void disable(int cap) {
        GL11.glDisable(cap);
    }

    public static void blendFunc(int sfactor, int dfactor) {
        GL11.glBlendFunc(sfactor, dfactor);
    }

    public static void depthMask(boolean flag) {
        GL11.glDepthMask(flag);
    }

    public static void setLightmapTextureCoords(int lightmapTexUnit, float x, float y) {
        OpenGlHelper.setLightmapTextureCoords(lightmapTexUnit, x, y);
    }

    public static void setActiveTexture(int id) {
        OpenGlHelper.setActiveTexture(id);
    }

    public static void enableLighting() {
        enable(GL11.GL_LIGHTING);
    }

    public static void disableLighting() {
        disable(GL11.GL_LIGHTING);
    }

    public static boolean getBoolean(int pname) {
        return GL11.glGetBoolean(pname);
    }

    public static void texParameter(int target, int pname, int param) {
        GL11.glTexParameteri(target, pname, param);
    }

    public static void texParameter(int target, int pname, float param) {
        GL11.glTexParameterf(target, pname, param);
    }

    public static void texParameter(int target, int pname, FloatBuffer buffer) {
        GL11.glTexParameter(target, pname, buffer);
    }

    public static void texParameter(int target, int pname, IntBuffer buffer) {
        GL11.glTexParameter(target, pname, buffer);
    }

    /**
     * @param resource - The ResourceLocation of which to get the GL texture ID from.
     * @return Returns the GL texture ID of the specified ResourceLocation
     */
    public static int getTextureId(ResourceLocation resource) {
        Object object = Minecraft.getMinecraft().getTextureManager().getTexture(resource);
        object = object == null ? new SimpleTexture(resource) : object;
        return ((ITextureObject) object).getGlTextureId();
    }

    public static void shadeSmooth() {
        GL11.glShadeModel(GL11.GL_SMOOTH);
    }

    public static void shadeFlat() {
        GL11.glShadeModel(GL11.GL_FLAT);
    }

    public static void enableRescaleNormal() {
        enable(GL12.GL_RESCALE_NORMAL);
    }

    public static void disableRescaleNormal() {
        disable(GL12.GL_RESCALE_NORMAL);
    }

    public static void enableStandardItemLighting() {
        RenderHelper.enableStandardItemLighting();
    }

    public static void disableStandardItemLighting() {
        RenderHelper.disableStandardItemLighting();
    }

    public static void enableAlphaTest() {
        enable(GL11.GL_ALPHA_TEST);
    }

    public static void disableAlphaTest() {
        disable(GL11.GL_ALPHA_TEST);
    }

    public static void readBuffer(int mode) {
        GL11.glReadBuffer(mode);
    }

    public static void readPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
        GL11.glReadPixels(x, y, width, height, format, type, pixels);
    }

    public static void enableCullFace() {
        enable(GL11.GL_CULL_FACE);
    }

    public static void disableCullFace() {
        disable(GL11.GL_CULL_FACE);
    }

    /**
     * Disable lightmapping, enable GL_BLEND, and reset the colors to default values.
     */
    public static void disableLightMapping() {
        char light = 61680;
        OpenGL.enableBlend();
        OpenGL.blendFunc(GL_ONE, GL_ONE);
        OpenGL.depthMask(true);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) light % 65536 / 1.0F, (float) light / 65536 / 1.0F);
        OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Enable lightmapping, disable GL_BLEND, and reset the colors to default values.
     */
    public static void enableLightMapping() {
        char light = 61680;
        OpenGL.disableBlend();
        OpenGL.depthMask(true);
        OpenGL.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) light % 65536 / 1.0F, (float) light / 65536 / 1.0F);
        OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Disable lighting
     */
    public static void disableLight() {
        OpenGL.setActiveTexture(OpenGL.lightmapTexUnit);
        if (lightmapTexUnitTextureEnable = OpenGL.getBoolean(GL11.GL_TEXTURE_2D)) {
            OpenGL.disableTexture2d();
        }
        OpenGL.setActiveTexture(OpenGlHelper.defaultTexUnit);
        OpenGL.disableLighting();
    }

    /**
     * Enable lighting
     */
    public static void enableLight() {
        OpenGL.setActiveTexture(OpenGL.lightmapTexUnit);
        if (lightmapTexUnitTextureEnable) {
            OpenGL.enableTexture2d();
        }
        OpenGL.setActiveTexture(OpenGL.defaultTexUnit);
        OpenGL.enableLighting();
    }

    public static void blendClear() {
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
    }

    /**
     * Combonation of GL functions used to smooth out the rough edges of a 2D texture.
     */
    public static void antiAlias2d() {
        OpenGL.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        OpenGL.texParameter(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        OpenGL.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        OpenGL.texParameter(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
    }

    public static void enableFog() {
        enable(GL11.GL_FOG);
    }

    public static void disableFog() {
        disable(GL11.GL_FOG);
    }

    public static void bindTexture(int target, int texture) {
        GL11.glBindTexture(target, texture);
    }

    public static void copyTexSubImage(int target, int level, int xoffset, int yoffset, int x, int y) {
        GL11.glCopyTexSubImage1D(target, level, xoffset, yoffset, x, y);
    }

    public static void copyTexSubImage(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
        GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
    }

    public static void copyDownsizedRender(TextureManager manager, ResourceLocation target, int x, int y, int w, int h, int index) {
        ITextureObject textureObject = manager.getTexture(target);

        if (textureObject != null) {
            OpenGL.bindTexture(GL11.GL_TEXTURE_2D, textureObject.getGlTextureId());
            OpenGL.copyTexSubImage(GL11.GL_TEXTURE_2D, 0, index, index, x, y, w, h);
        }
    }

    public static Framebuffer createFrameBuffer(int width, int height, boolean useDepth) {
        Framebuffer render = new Framebuffer(width, height, useDepth);
        frameBuffers.add(render);
        return render;
    }

    public static void destroyFrameBuffer(Framebuffer buffer) {
        OpenGL.enableDepthTest();
        if (buffer.framebufferObject >= 0) {
            buffer.deleteFramebuffer();
        }
        frameBuffers.remove(buffer);
    }

}
