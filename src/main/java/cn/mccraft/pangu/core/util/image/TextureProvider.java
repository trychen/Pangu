package cn.mccraft.pangu.core.util.image;

import net.minecraft.util.ResourceLocation;

public interface TextureProvider {
    ResourceLocation getTexture();
    ResourceLocation getTexture(ResourceLocation loading);
    ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error);

    static TextureProvider of(String path, ResourceLocation missing) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return RemoteImage.of(path, missing);
        } else if (path.startsWith("local:")) {
            return new BuiltinImage(new ResourceLocation(path));
        }

        return null;
    }
}
