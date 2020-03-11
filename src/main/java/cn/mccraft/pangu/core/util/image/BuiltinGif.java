package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BuiltinGif extends GifImage {
    private ResourceLocation path;

    public BuiltinGif(ResourceLocation path) {
        this.path = path;
        this.id = path.toString();
    }

    @Override
    public void readGifImage(GifDecoder decoder) {
        try {
            IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(path);
            if (resource == null) {
                throw new FileNotFoundException("Cannot find resource " + path.toString());
            }
            decoder.read(resource.getInputStream());
        } catch (IOException e) {
            PanguCore.getLogger().error("Couldn't load BuiltinGif", e);
            exception = true;
        }
    }
}
