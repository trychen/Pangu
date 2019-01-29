package cn.mccraft.pangu.core.util.image;

import net.minecraft.util.ResourceLocation;

import java.net.MalformedURLException;

public interface TextureProvider {
    ResourceLocation getTexture();

    static TextureProvider of(String path, ResourceLocation missing) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            try {
                return new RemoteImage(path, missing);
            } catch (MalformedURLException e) {
            }
        } else if (path.startsWith("local:")) {
            return new BuiltinImage(new ResourceLocation(path));
        }

        return null;
    }
}
