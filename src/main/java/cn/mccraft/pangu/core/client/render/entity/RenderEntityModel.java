package cn.mccraft.pangu.core.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

import javax.annotation.Nullable;

public class RenderEntityModel extends Render {
    private ITextureProvider textureProvider;

    public RenderEntityModel(RenderManager renderManager, ModelBase modelBase, ITextureProvider textureProvider) {
        super(renderManager);
        throw new NotImplementedException("Don't support rendering entity with ModelBase for now!");
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return textureProvider == null ? null : textureProvider.getTexture(entity);
    }
}
