package cn.mccraft.pangu.core.util.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
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
public class RenderUtils {
    private static final Minecraft minecraft = Minecraft.getMinecraft();
    
    /**
     * Render Item with {@link net.minecraft.client.renderer.RenderItem} as its on the ground
     */
    public static void renderItem(ItemStack item) {
        renderItem(item, ItemCameraTransforms.TransformType.GROUND);
    }

    /**
     * Render Item with {@link net.minecraft.client.renderer.RenderItem}
     */
    public static void renderItem(ItemStack item, ItemCameraTransforms.TransformType transformType) {
        minecraft.getRenderItem().renderItem(item, transformType);
    }

    /**
     * Render Block with {@link Tessellator} and {@link BlockRendererDispatcher}
     */
    public static void renderBlock(IBlockState state, BlockPos pos) {
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
    public static void renderEntity(Entity entity, double x, double y, double z, float yaw, float partialTicks, boolean box) {
        minecraft.getRenderManager().doRenderEntity(entity, x, y, z, yaw, partialTicks, box);
    }

    /**
     * render entity in Vec ZERO with no
     */
    public static void renderEntity(Entity entity, float partialTick) {
        renderEntity(entity, 0, 0, 0, 0, partialTick, false);
    }

    /**
     * get the render timer ticks
     * @return
     */
    public static float getRenderPartialTicks() {
        return minecraft.getRenderPartialTicks();
    }

    /**
     * a simple util to helper you rotate translate, and this is also a box render
     */
    public static class Draw3D {

        public static void rotateX() {
            rotate(180, 1, 0, 0);
        }

        public static void rotateY() {
            rotate(180, 0, 1, 0);
        }

        public static void rotateZ() {
            rotate(180, 0, 0, 1);
        }

        public static void rotateX(float rot) {
            rotate(rot, 1, 0, 0);
        }

        public static void rotateY(float rot) {
            rotate(rot, 0, 1, 0);
        }

        public static void rotateZ(float rot) {
            rotate(rot, 0, 0, 1);
        }

        public static void translate(Entity entity) {
            GlStateManager.translate((float) entity.posX, (float) entity.posY, (float) entity.posZ);
        }

        public static void translate(BlockPos pos) {
            GlStateManager.translate(pos.getX(), pos.getY(), pos.getZ());
        }

        public static void translateCenter(BlockPos pos) {
            GlStateManager.translate(pos.getX() + .5F, pos.getY() + .5F, pos.getZ() + .5F);
        }

        /**
         * draw rectangle in face with the size of x, y, z
         */
        public static void drawRectangle(float x, float y, float z, EnumFacing facing) {
            glBegin(GL_QUADS);
            switch (facing) {
                case UP:
                    glVertex3f(x, y, z);
                    glVertex3f(-x, y, z);
                    glVertex3f(-x, y, -z);
                    glVertex3f(x, y, -z);
                    break;
                case DOWN:
                    glVertex3f(x, -y, z);
                    glVertex3f(-x, -y, z);
                    glVertex3f(-x, -y, -z);
                    glVertex3f(x, -y, -z);
                    break;
                case NORTH:
                    glVertex3f(x, y, z);
                    glVertex3f(-x, y, z);
                    glVertex3f(-x, -y, z);
                    glVertex3f(x, -y, z);
                    break;
                case SOUTH:
                    glVertex3f(x, y, z);
                    glVertex3f(-x, y, z);
                    glVertex3f(-x, -y, z);
                    glVertex3f(x, -y, z);
                    break;
                case WEST:
                    glVertex3f(x, y, z);
                    glVertex3f(x, -y, z);
                    glVertex3f(x, -y, -z);
                    glVertex3f(x, y, -z);
                    break;
                case EAST:
                    glVertex3f(-x, y, z);
                    glVertex3f(-x, -y, z);
                    glVertex3f(-x, -y, -z);
                    glVertex3f(-x, y, -z);
                    break;
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
         */
        public static void drawCube(float x, float y, float z) {
            for (EnumFacing facing : EnumFacing.VALUES) drawRectangle(x, y, z, facing);
        }

    }
}
