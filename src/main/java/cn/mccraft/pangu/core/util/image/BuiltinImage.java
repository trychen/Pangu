package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class BuiltinImage implements TextureProvider {
    private final ResourceLocation res;

    public BuiltinImage(String res) {
        this(PanguResLoc.of(res));
    }

    public BuiltinImage(ResourceLocation res) {
        this.res = res;
    }

    @Override
    public ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error) {
        return res;
    }

    public static BuiltinImage of(String domain, String path) {
        return new BuiltinImage(PanguResLoc.of(domain, path));
    }

    @Override
    public TextureAtlasSprite asAtlasSprite() {
        try {
            IResource resource = Games.minecraft().getResourceManager().getResource(res);
            PackagedTextureAtlasSprite texture = new PackagedTextureAtlasSprite();
            if (texture.initPackage(resource.getInputStream())) return texture;
        } catch (IOException e) {
            PanguCore.getLogger().error("Error while load resource " + res.toString(), e);
        }
        return null;
    }
}
