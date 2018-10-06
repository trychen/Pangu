package cn.mccraft.pangu.core.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderEntityLivingModel extends RenderLiving<EntityLiving> {
    private ITextureProvider textureProvider;

    public RenderEntityLivingModel(RenderManager renderManager, ModelBase modelBase, float shadowSize, ITextureProvider textureProvider) {
        super(renderManager, modelBase, shadowSize);
        this.textureProvider = textureProvider;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityLiving entity) {
        return textureProvider == null ? null : textureProvider.getTexture(entity);
    }
}
