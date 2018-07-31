package cn.mccraft.pangu.core.client.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public interface ITextureProvider {
    ResourceLocation getTexture(Entity entity);
}
