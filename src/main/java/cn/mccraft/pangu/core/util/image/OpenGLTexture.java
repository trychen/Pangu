package cn.mccraft.pangu.core.util.image;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpenGLTexture extends OpenGLTextureProvider {
    private final int texture;

    @Override
    public int getTextureID() {
        return texture;
    }
}