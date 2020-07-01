package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.Games;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class PackagedTextureAtlasSprite extends TextureAtlasSprite {
    protected BufferedImage image;

    protected PackagedTextureAtlasSprite() {
        super("NA");
    }

    public boolean initPackage(File file) {
        try {
            return initPackage(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            PanguCore.getLogger().error(file.getName(), e);
            return false;
        }
    }

    public boolean initPackage(InputStream stream) {
        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            PanguCore.getLogger().error("Error while init PackagedTextureAtlasSprite", e);
            return false;
        }
        this.width = this.height = Math.max(image.getHeight(), image.getWidth());
        if (image.getWidth() != image.getHeight()) {
            BufferedImage scaleImage = new BufferedImage(this.width, this.width, BufferedImage.TYPE_4BYTE_ABGR_PRE);
            Graphics2D tGraphics = scaleImage.createGraphics();
            tGraphics.drawImage(image, (this.width - image.getWidth()) / 2, (this.width - image.getHeight()) / 2, null);
            tGraphics.dispose();
            this.image = scaleImage;
        }
        this.initSprite(this.width, this.height, 0, 0, false);
        return true;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
        super.initSprite(inX, inY, originInX, originInY, rotatedIn);

        int[][] tPixels = new int[Games.minecraft().gameSettings.mipmapLevels + 1][];
        tPixels[0] = new int[this.getIconWidth() * this.getIconHeight()];
        this.image.getRGB(originInX, originInY, this.getIconWidth(), this.getIconHeight(), tPixels[0], 0, this.getIconWidth());
        this.clearFramesTextureData();
        this.framesTextureData.add(tPixels);
    }
}
