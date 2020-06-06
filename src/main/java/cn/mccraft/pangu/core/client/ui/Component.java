package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.ui.style.Style;
import cn.mccraft.pangu.core.util.render.Rect;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Accessors(chain = true)
@ToString
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
    @Setter
    protected float height = 0, width = 0;

    @Setter
    private float x = 0, y = 0;

    @Getter
    @Setter
    protected boolean focused = false;

    @Getter
    @Setter
    protected boolean hovered = false, visible = true, disabled = false;

    @Setter
    @Getter
    protected List<String> toolTips;

    @Setter
    protected Set<Style> styles;

    public Component() {
    }

    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
    }

    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
    }

    public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
    }

    /**
     * Only if this component is focused ({@link Container#focus(Component)})
     */
    public void onKeyTyped(char typedChar, int keyCode) {
    }

    /**
     * Mouse scrolling.
     */
    public void onMouseInput(int mouseX, int mouseY) {
    }

    public void onUpdate(int mouseX, int mouseY) {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.getX()
                && mouseY >= this.getY()
                && mouseX < this.getX() + this.getWidth()
                && mouseY < this.getY() + this.getHeight();
    }

    public float getX() {
        if (getParent() instanceof Container) return getNativeX() + ((Container) getParent()).getOffsetX();
        else if (getParent() instanceof TabContainer) return getNativeX() + getParent().getX();
        return getNativeX();
    }

    public float getY() {
        if (getParent() instanceof Container) return getNativeY() + ((Container) getParent()).getOffsetY();
        else if (getParent() instanceof TabContainer) return getNativeY() + getParent().getY();
        else return getNativeY();
    }

    public float getNativeX() {
        if (getScreen() != null && getScreen().centerOrigin) return getScreen().halfWidth + x;
        return x;
    }

    public float getNativeY() {
        if (getScreen() != null && getScreen().centerOrigin) return getScreen().halfHeight + y;
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

    public Component getRoot() {
        if (parent == null) return this;
        else return parent.getParent();
    }

    /**
     * Draw a red frame that contains this component
     */
    @SideOnly(Side.CLIENT)
    @Deprecated
    public void drawComponentBox() {
        drawComponentBox(0xFFFF0000);
    }

    /**
     * Draw a frame that contains this component
     */
    @SideOnly(Side.CLIENT)
    public void drawComponentBox(int color) {
        Rect.drawFrameBox(getX(), getY(), getWidth(), getHeight(), 1, color);
    }

    /**
     * Draw tooltips
     */
    @SideOnly(Side.CLIENT)
    public void drawToolTips(List<String> texts, int mouseX, int mouseY) {
        if (getScreen() != null) {
            getScreen().drawHovering(this, texts, mouseX, mouseY);
        } else {
            GuiUtils.drawHoveringText(texts, mouseX, mouseY, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, -1, Minecraft.getMinecraft().fontRenderer);
            RenderHelper.disableStandardItemLighting();
        }
    }

    @SideOnly(Side.CLIENT)
    public void playPressSound() {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public Component toolTips(String... toolTips) {
        setToolTips(Lists.newArrayList(toolTips));
        return this;
    }

    public Set<Style> getStyles() {
        if (styles == null) return Collections.emptySet();
        return styles;
    }

    @Override
    public int compareTo(Component o) {
        return Integer.compare(this.getZLevel(), o.getZLevel());
    }
}
