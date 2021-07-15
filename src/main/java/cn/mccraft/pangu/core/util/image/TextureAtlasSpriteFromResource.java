package cn.mccraft.pangu.core.util.image;

import com.googlecode.pngtastic.core.PngImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class TextureAtlasSpriteFromResource extends TextureAtlasSprite {
    protected int[] buf;

    public TextureAtlasSpriteFromResource(ResourceLocation resource) throws IOException {
        super(resource.toString());
        IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);
        PngImage png = new PngImage(res.getInputStream());
        this.width = (int) png.getWidth();
        this.height = (int) png.getHeight();

        this.buf = PngReader.INSTANCE.readARGB8(png);
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    public boolean load(IResourceManager manager, ResourceLocation location) {
        int[][] aint = new int[Minecraft.getMinecraft().getTextureMapBlocks().getMipmapLevels() + 1][];
        aint[0] = buf;

        this.framesTextureData.add(aint);

        this.initSprite(width, height, 0, 0, false);

        return false;
    }
}
