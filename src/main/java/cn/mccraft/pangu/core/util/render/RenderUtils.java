package cn.mccraft.pangu.core.util.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraft.client.renderer.GlStateManager.*;
import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * An simple Render Helper to help you render something
 */
@SideOnly(Side.CLIENT)
public interface RenderUtils {
    Minecraft minecraft = Minecraft.getMinecraft();

    /**
     * Render Item with {@link net.minecraft.client.renderer.RenderItem} as its on the ground
     */
    static void renderItem(ItemStack item) {
        renderItem(item, ItemCameraTransforms.TransformType.GROUND);
    }

    /**
     * Render Item with {@link net.minecraft.client.renderer.RenderItem}
     */
    static void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
        minecraft.getRenderItem().renderItem(item, transformType);
    }

    /**
     * Render Block with {@link Tessellator} and {@link BlockRendererDispatcher}
     */
    static void renderBlock(IBlockState state, BlockPos pos) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder = tessellator.getBuffer();
        BufferBuilder.begin(GL_QUADS, DefaultVertexFormats.BLOCK);
        BlockRendererDispatcher dispatcher = minecraft.getBlockRendererDispatcher();
        dispatcher.renderBlock(state, pos, minecraft.world, BufferBuilder);
        tessellator.draw();
    }

    /**
     * do entity render in x, y, z with yaw, partialTicks
     *
     * @param box if render the debug bounding box. required debugBoundingBox enabled
     */
    static void renderEntity(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean box) {
        minecraft.getRenderManager().renderEntity(entity, x, y, z, yaw, partialTicks, box);
    }

    /**
     * render entity in Vec ZERO with no
     */
    static void renderEntity(Entity entity, float partialTick) {
        renderEntity(entity, 0, 0, 0, 0, partialTick, false);
    }

    /**
     * get the render timer ticks
     */
    static float getRenderPartialTicks() {
        return minecraft.getRenderPartialTicks();
    }

    static void rotateX() {
        rotate(90, 1, 0, 0);
    }

    static void rotateY() {
        rotate(90, 0, 1, 0);
    }

    static void rotateZ() {
        rotate(90, 0, 0, 1);
    }

    static void rotateX(float rot) {
        rotate(rot, 1, 0, 0);
    }

    static void rotateY(float rot) {
        rotate(rot, 0, 1, 0);
    }

    static void rotateZ(float rot) {
        rotate(rot, 0, 0, 1);
    }

    static void translate(Entity entity) {
        GlStateManager.translate((float) entity.posX, (float) entity.posY, (float) entity.posZ);
    }

    static void translate(BlockPos pos) {
        GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
    }

    static void translateCenter(BlockPos pos) {
        GlStateManager.translate(pos.getX() + .5F, pos.getY() + .5F, pos.getZ() + .5F);
    }

    /**
     * draw rectangle in face with the size of x, y, z
     * <p>
     * 渲染一个大小为x, y, z的矩形
     */
    static void drawRectangle(float x, float y, float z, EnumFacing facing) {
        glBegin(GL_QUADS);

        if (facing == EnumFacing.UP) {
            glVertex3f(x, y, z);
            glVertex3f(-x, y, z);
            glVertex3f(-x, y, -z);
            glVertex3f(x, y, -z);
        } else if (facing == EnumFacing.DOWN) {
            glVertex3f(x, -y, z);
            glVertex3f(-x, -y, z);
            glVertex3f(-x, -y, -z);
            glVertex3f(x, -y, -z);
        } else if (facing == EnumFacing.NORTH) {
            glVertex3f(x, y, z);
            glVertex3f(-x, y, z);
            glVertex3f(-x, -y, z);
            glVertex3f(x, -y, z);
        } else if (facing == EnumFacing.SOUTH) {
            glVertex3f(x, y, z);
            glVertex3f(-x, y, z);
            glVertex3f(-x, -y, z);
            glVertex3f(x, -y, z);
        } else if (facing == EnumFacing.WEST) {
            glVertex3f(x, y, z);
            glVertex3f(x, -y, z);
            glVertex3f(x, -y, -z);
            glVertex3f(x, y, -z);
        } else if (facing == EnumFacing.EAST) {
            glVertex3f(-x, y, z);
            glVertex3f(-x, -y, z);
            glVertex3f(-x, -y, -z);
            glVertex3f(-x, y, -z);
        }
        glEnd();

        glBegin(GL_QUADS);
        switch (facing) {
            case UP:
                glVertex3f(x, y, -z);
                glVertex3f(-x, y, -z);
                glVertex3f(-x, y, z);
                glVertex3f(x, y, z);
                break;
            case DOWN:
                glVertex3f(x, -y, -z);
                glVertex3f(-x, -y, -z);
                glVertex3f(-x, -y, z);
                glVertex3f(x, -y, z);
                break;
            case NORTH:
                glVertex3f(x, -y, z);
                glVertex3f(-x, -y, z);
                glVertex3f(-x, y, z);
                glVertex3f(x, y, z);
                break;
            case SOUTH:
                glVertex3f(x, -y, z);
                glVertex3f(-x, -y, z);
                glVertex3f(-x, y, z);
                glVertex3f(x, y, z);
                break;
            case WEST:
                glVertex3f(x, y, -z);
                glVertex3f(x, -y, -z);
                glVertex3f(x, -y, z);
                glVertex3f(x, y, z);
                break;
            case EAST:
                glVertex3f(-x, y, -z);
                glVertex3f(-x, -y, -z);
                glVertex3f(-x, -y, z);
                glVertex3f(-x, y, z);
                break;
        }
        glEnd();
    }

    /**
     * Draw an cube with the size of x, y, z
     * <p>
     * 渲染一个大小为 x, y, z 的立方体
     */
    static void drawCube(float x, float y, float z) {
        // White side - BACK
        glBegin(GL_QUADS);
        color(1, 1, 1);
        glVertex3f(x, -y, z);
        glVertex3f(x, y, z);
        glVertex3f(-x, y, z);
        glVertex3f(-x, -y, z);
        glEnd();

        // Purple side - RIGHT
        glBegin(GL_QUADS);
        color(1, 0, 1);
        glVertex3f(x, -y, -z);
        glVertex3f(x, y, -z);
        glVertex3f(x, y, z);
        glVertex3f(x, -y, z);
        glEnd();

        // Green side - LEFT
        glBegin(GL_QUADS);
        color(0, 1, 0);
        glVertex3f(-x, -y, z);
        glVertex3f(-x, y, z);
        glVertex3f(-x, y, -z);
        glVertex3f(-x, -y, -z);
        glEnd();

        // Blue side - TOP
        glBegin(GL_QUADS);
        color(0, 0, 1);
        glVertex3f(x, y, z);
        glVertex3f(x, y, -z);
        glVertex3f(-x, y, -z);
        glVertex3f(-x, y, z);
        glEnd();

        // Red side - BOTTOM
        glBegin(GL_QUADS);
        color(1, 0, 0);
        glVertex3f(x, -y, -z);
        glVertex3f(x, -y, z);
        glVertex3f(-x, -y, z);
        glVertex3f(-x, -y, -z);
        glEnd();

//        for (EnumFacing facing : EnumFacing.VALUES) drawRectangle(x, y, z, facing);
    }

    static float alpha(int color) {
        return (color >> 24 & 255) / 255.0F;
    }
    static float red(int color) {
        return (color >> 16 & 255) / 255.0F;
    }
    static float blue(int color) {
        return (color >> 8 & 255) / 255.0F;
    }
    static float green(int color) {
        return (color & 255) / 255.0F;
    }

    static int alphaInt(int color) {
        return color >> 24 & 255;
    }
    static int redInt(int color) {
        return color >> 16 & 255;
    }
    static int blueInt(int color) {
        return color >> 8 & 255;
    }
    static int greenInt(int color) {
        return color & 255;
    }
}
