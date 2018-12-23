package cn.mccraft.pangu.core.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.NotImplementedException;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class RenderEntityModel extends Render<Entity> {
    private ITextureProvider textureProvider;
    private ModelBase modelBase;

    public RenderEntityModel(RenderManager renderManager, ModelBase modelBase, ITextureProvider textureProvider) {
        super(renderManager);
        this.modelBase = modelBase;
    }

    @Override
    public void doRender(@Nonnull Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        bindEntityTexture(entity);
        modelBase.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull Entity entity) {
        return textureProvider == null ? null : textureProvider.getTexture(entity);
    }
}
