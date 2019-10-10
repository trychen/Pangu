package cn.mccraft.pangu.core.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public interface Box {
    static void offset2View(Runnable runnable, float partialTicks) {
        Entity view = Minecraft.getMinecraft().getRenderViewEntity();
        double x = view.lastTickPosX + ((view.posX - view.lastTickPosX) * partialTicks);
        double y = view.lastTickPosY + ((view.posY - view.lastTickPosY) * partialTicks);
        double z = view.lastTickPosZ + ((view.posZ - view.lastTickPosZ) * partialTicks);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-x, -y, -z);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();

        runnable.run();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    static void quads(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder render = tessellator.getBuffer();

        render.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        // top
        render.pos(box.maxX, box.maxY, box.minZ).endVertex();
        render.pos(box.minX, box.maxY, box.minZ).endVertex();
        render.pos(box.minX, box.maxY, box.maxZ).endVertex();
        render.pos(box.maxX, box.maxY, box.maxZ).endVertex();

        // bottom
        render.pos(box.maxX, box.minY, box.maxZ).endVertex();
        render.pos(box.minX, box.minY, box.maxZ).endVertex();
        render.pos(box.minX, box.minY, box.minZ).endVertex();
        render.pos(box.maxX, box.minY, box.minZ).endVertex();

        // north
        render.pos(box.maxX, box.minY, box.minZ).endVertex();
        render.pos(box.minX, box.minY, box.minZ).endVertex();
        render.pos(box.minX, box.maxY, box.minZ).endVertex();
        render.pos(box.maxX, box.maxY, box.minZ).endVertex();

        // south
        render.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        render.pos(box.minX, box.maxY, box.maxZ).endVertex();
        render.pos(box.minX, box.minY, box.maxZ).endVertex();
        render.pos(box.maxX, box.minY, box.maxZ).endVertex();

        // west
        render.pos(box.minX, box.maxY, box.maxZ).endVertex();
        render.pos(box.minX, box.maxY, box.minZ).endVertex();
        render.pos(box.minX, box.minY, box.minZ).endVertex();
        render.pos(box.minX, box.minY, box.maxZ).endVertex();

        // east
        render.pos(box.maxX, box.minY, box.maxZ).endVertex();
        render.pos(box.maxX, box.minY, box.minZ).endVertex();
        render.pos(box.maxX, box.maxY, box.minZ).endVertex();
        render.pos(box.maxX, box.maxY, box.maxZ).endVertex();

        tessellator.draw();
    }

    static void lines(AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder render = tessellator.getBuffer();

        render.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        // top
        //  -
        render.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        render.pos(box.minX, box.maxY, box.maxZ).endVertex();

        //  -
        // |
        render.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        render.pos(box.maxX, box.maxY, box.minZ).endVertex();

        //  -
        // |
        //  -
        render.pos(box.maxX, box.maxY, box.minZ).endVertex();
        render.pos(box.minX, box.maxY, box.minZ).endVertex();

        //  -
        // | |
        //  -
        render.pos(box.minX, box.maxY, box.maxZ).endVertex();
        render.pos(box.minX, box.maxY, box.minZ).endVertex();

        // bottom
        //  -
        render.pos(box.maxX, box.minY, box.maxZ).endVertex();
        render.pos(box.minX, box.minY, box.maxZ).endVertex();

        //  -
        // |
        render.pos(box.maxX, box.minY, box.maxZ).endVertex();
        render.pos(box.maxX, box.minY, box.minZ).endVertex();

        //  -
        // |
        //  -
        render.pos(box.maxX, box.minY, box.minZ).endVertex();
        render.pos(box.minX, box.minY, box.minZ).endVertex();

        //  -
        // | |
        //  -
        render.pos(box.minX, box.minY, box.maxZ).endVertex();
        render.pos(box.minX, box.minY, box.minZ).endVertex();

        // side
        render.pos(box.maxX, box.maxY, box.maxZ).endVertex();
        render.pos(box.maxX, box.minY, box.maxZ).endVertex();

        render.pos(box.maxX, box.maxY, box.minZ).endVertex();
        render.pos(box.maxX, box.minY, box.minZ).endVertex();

        render.pos(box.minX, box.maxY, box.maxZ).endVertex();
        render.pos(box.minX, box.minY, box.maxZ).endVertex();

        render.pos(box.minX, box.maxY, box.minZ).endVertex();
        render.pos(box.minX, box.minY, box.minZ).endVertex();

        tessellator.draw();
    }
}
