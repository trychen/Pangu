package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Accessors(chain = true)
public abstract class Component implements Cloneable, Comparable<Component> {
    @Getter
    @Setter
    protected Component parent;

    @Getter
    @Setter
    protected Screen screen;

    @Getter
    @Setter
    protected int zLevel = 100;

    @Getter
    protected float height = 0, width = 0;

    @Setter
    private float x = 0, y = 0;

    @Getter
    @Setter
    protected boolean hovered = false, visible = true, disabled = false;

    public Component() {
    }

    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
    }

    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
    }

    public void onMouseReleased(int mouseX, int mouseY) {
    }

    public void onKeyTyped(char typedChar, int keyCode) {
    }

    public void onUpdate(int mouseX, int mouseY) {
        this.hovered = isHovered(mouseX, mouseY);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.getX()
                && mouseY >= this.getY()
                && mouseX < this.getX() + this.width
                && mouseY < this.getY() + this.height;
    }

    public float getX() {
        if (parent instanceof Container) return getNativeX() + ((Container) parent).getOffsetX();
        return getNativeX();
    }

    public float getY() {
        if (parent instanceof Container) return getNativeY() + ((Container) parent).getOffsetY();
        return getNativeY();
    }

    public float getNativeX() {
        return x;
    }

    public float getNativeY() {
        return y;
    }

    public Component setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Component setCenteredPosition(float x, float y) {
        return setPosition(x - width / 2, y - height / 2);
    }

    public Component setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Nullable
    public List<String> getToolTips() {
        return null;
    }

    public void drawComponentBox() {
        Rect.drawFrameBox(x, y, width, height, 1, 0xFFFF0000);
    }

    /**
     * Draw tooltips
     */
    public void drawToolTips(List<String> texts, int mouseX, int mouseY) {
        GuiUtils.drawHoveringText(texts, mouseX, mouseY, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, -1, Minecraft.getMinecraft().fontRenderer);
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public int compareTo(Component o) {
        return Integer.compare(this.getZLevel(), o.getZLevel());
    }
}
