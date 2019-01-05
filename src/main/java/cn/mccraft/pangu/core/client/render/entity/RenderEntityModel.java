package cn.mccraft.pangu.core.client.render.entity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderEntityModel extends Render<Entity> {
    @Setter
    private ITextureProvider textureProvider;
    @Setter
    @Getter
    private ModelBase mainModel;

    public RenderEntityModel(RenderManager renderManager, ModelBase mainModel, ITextureProvider textureProvider) {
        super(renderManager);
        this.mainModel = mainModel;
    }

    @Override
    public void doRender(@Nonnull Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        bindEntityTexture(entity);
        mainModel.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return textureProvider == null ? null : textureProvider.getTexture(entity);
    }
}
