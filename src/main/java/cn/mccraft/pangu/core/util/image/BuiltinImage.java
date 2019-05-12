package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.util.ResourceLocation;

public class BuiltinImage implements TextureProvider {
    private final ResourceLocation res;

    public BuiltinImage(ResourceLocation res) {
        this.res = res;
    }

    @Override
    public ResourceLocation getTexture() {
        return res;
    }

    @Override
    public ResourceLocation getTexture(ResourceLocation loading) {
        return getTexture();
    }

    @Override
    public ResourceLocation getTexture(ResourceLocation loading, ResourceLocation error) {
        return getTexture();
    }

    public static BuiltinImage of(String domain, String path) {
        return new BuiltinImage(PanguResLoc.of(domain, path));
    }
}
