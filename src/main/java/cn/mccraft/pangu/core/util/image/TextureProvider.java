package cn.mccraft.pangu.core.util.image;

import net.minecraft.util.ResourceLocation;

public interface TextureProvider {
    ResourceLocation getTexture();
    ResourceLocation getTexture(ResourceLocation loading);
    ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error);

    static TextureProvider of(String path, ResourceLocation missing) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return RemoteImage.of(path, missing);
        } else if (path.startsWith("location:")) {
            return new BuiltinImage(new ResourceLocation(path.substring(6)));
        } else if (path.startsWith("file:")) {
            return new BuiltinImage(new ResourceLocation(path.substring(2)));
        }

        return new BuiltinImage(missing);
    }
}
