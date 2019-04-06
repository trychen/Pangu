package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.io.File;

public interface TextureProvider {
    ResourceLocation getTexture();
    ResourceLocation getTexture(ResourceLocation loading);
    ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error);
    int[] ID = new int[] {0};
    static TextureProvider of(String path, ResourceLocation missing) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return RemoteImage.of(path, missing);
        } else if (path.startsWith("location:")) {
            return new BuiltinImage(new ResourceLocation(path.substring(9)));
        } else if (path.startsWith("file:")) {
            File file = new File(path.substring(5));
            if (!file.exists()) {
                PanguCore.getLogger().error("couldn't load image from " + path + ", file not exists");
            } else {
                try {
                    ResourceLocation location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("pangu_file_image_" + ID[0], new DynamicTexture(ImageIO.read(file)));
                    ID[0]++;
                    return new BuiltinImage(location);
                } catch (Exception e) {
                    PanguCore.getLogger().error("error while loading image from file " + path, e);
                }
            }
        }

        return new BuiltinImage(missing);
    }
}
