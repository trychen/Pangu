package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class OpenGLTextureProvider implements TextureProvider {
    public abstract int getTextureID();

    @Override
    public void bind() {
        int textureID = getTextureID();
        if (textureID > 0) GlStateManager.bindTexture(textureID);
    }

    @Override
    public void bind(ResourceLocation loading) {
        int textureID = getTextureID();
        if (textureID > 0) GlStateManager.bindTexture(textureID);
        else Rect.bind(loading);
    }

    @Override
    public void bind(ResourceLocation loading, ResourceLocation error) {
        int textureID = getTextureID();
        if (textureID > 0) GlStateManager.bindTexture(textureID);
        else if (textureID == 0) Rect.bind(loading);
        else Rect.bind(error);
    }
}
