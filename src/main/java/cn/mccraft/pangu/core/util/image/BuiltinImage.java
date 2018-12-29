package cn.mccraft.pangu.core.util.image;

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
}