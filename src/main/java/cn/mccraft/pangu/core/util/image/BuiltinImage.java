package cn.mccraft.pangu.core.util.image;

import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.util.ResourceLocation;

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
}
