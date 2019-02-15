package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ScrollingList extends Component {
    public static final int SCROLL_BAR_WIDTH = 6;
    protected float scrollFactor;
    protected float initialMouseClickY = -2.0F;
    @Getter
    protected float scrollDistance;

    @Getter
    @Setter
    protected float generalScrollingDistance = 8;

    @Getter
    @Setter
    protected boolean highlightSelected = true;

    @Delegate
    @Getter
    protected List<Entry> entries = new ArrayList<>();

    @Getter
    protected int selectedIndex = -1;

    @Getter
    @Setter
    protected boolean showScrollBar = true;

    public ScrollingList(int height, int width) {
        setSize(width, height);
    }

    public int getContentHeight() {
        return entries.stream().mapToInt(it -> it.getEntryHeight()).sum();
    }

    public float getContentWidth() {
        if (isShowScrollBar())
            return getWidth() - SCROLL_BAR_WIDTH;
        return getWidth();
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {

        float scrollBarLeft = getX() + getContentWidth();
        float scrollBarRight = scrollBarLeft + SCROLL_BAR_WIDTH;

        if (Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovered()) {
                    float mouseListY = mouseY - getY() + this.scrollDistance;
                    float currentY = 0;
                    for (int i = 0; i < getEntries().size(); i++) {
                        Entry entry = getEntries().get(i);
                        if (mouseListY >= currentY && mouseListY <= (currentY + entry.getEntryHeight())) {
                            select(i);
                        }
                        currentY += entry.getEntryHeight();
                    }
//                    int slotIndex = mouseListY / this.slotHeight;
//
//                    if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength) {
//                        this.elementClicked(slotIndex, slotIndex == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L);
//                        this.selectedIndex = slotIndex;
//                        this.lastClickTime = System.currentTimeMillis();
//                    }

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {
                        this.scrollFactor = -1.0F;
                        float scrollHeight = this.getContentHeight() - getHeight();
                        if (scrollHeight < 1) scrollHeight = 1;

                        float var13 = (getHeight() * getHeight()) / this.getContentHeight();

                        if (var13 < 32) var13 = 32;
                        if (var13 > getHeight()) var13 = getHeight();

                        this.scrollFactor /= (getHeight() - var13) / (float) scrollHeight;
                    } else {
                        this.scrollFactor = 1.0F;
                    }

                    this.initialMouseClickY = mouseY;
                } else {
                    this.initialMouseClickY = -2.0F;
                }
            } else if (this.initialMouseClickY >= 0.0F) {
                this.scrollDistance -= ((float) mouseY - this.initialMouseClickY) * this.scrollFactor;
                this.initialMouseClickY = (float) mouseY;
            }
        } else {
            this.initialMouseClickY = -1.0F;
        }

        applyScrollLimits();

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        Minecraft client = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(client);

        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (getX() * scaleW), (int) (client.displayHeight - ((getY() + getHeight()) * scaleH)),
                (int) (getWidth() * scaleW), (int) (getHeight() * scaleH));

        float baseY = this.getY() - this.scrollDistance;

        float slotTop = baseY;
        for (int index = 0; index < entries.size(); index++) {
            Entry entry = entries.get(index);

            if (slotTop <= getY() + getHeight() && slotTop + entry.getEntryHeight() >= getY()) {
                if (this.highlightSelected && this.isSelected(index)) {
                    Rect.drawFrame(getX(), slotTop, getX() + getContentWidth(), slotTop + entry.getEntryHeight(), 1, 0xFF808080);
                }
                entry.onEntryDraw(this, index, getX(), slotTop, getContentWidth());
            }

            slotTop += entry.getEntryHeight();
        }

        // Scrolling bar
        float extraHeight = this.getContentHeight() - getHeight();

        if (isShowScrollBar() && extraHeight > 0) {
            float height = (getHeight() * getHeight()) / this.getContentHeight();

            if (height > getHeight()) height = getHeight();

            if (height < 32) height = 32;

            float barTop = this.scrollDistance * (getHeight() - height) / extraHeight + getY();
            if (barTop < getY()) barTop = getY();

            GlStateManager.disableTexture2D();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(scrollBarLeft, getY() + getHeight(), 0.0D).tex(0.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            buffer.pos(scrollBarRight, getY() + getHeight(), 0.0D).tex(1.0D, 1.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            buffer.pos(scrollBarRight, getY(), 0.0D).tex(1.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            buffer.pos(scrollBarLeft, getY(), 0.0D).tex(0.0D, 0.0D).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(scrollBarLeft, barTop + height, 0.0D).tex(0.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            buffer.pos(scrollBarRight, barTop + height, 0.0D).tex(1.0D, 1.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            buffer.pos(scrollBarRight, barTop, 0.0D).tex(1.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            buffer.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            tess.draw();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(scrollBarLeft, barTop + height - 1, 0.0D).tex(0.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            buffer.pos(scrollBarRight - 1, barTop + height - 1, 0.0D).tex(1.0D, 1.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            buffer.pos(scrollBarRight - 1, barTop, 0.0D).tex(1.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            buffer.pos(scrollBarLeft, barTop, 0.0D).tex(0.0D, 0.0D).color(0xC0, 0xC0, 0xC0, 0xFF).endVertex();
            tess.draw();
            GlStateManager.enableTexture2D();
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private void applyScrollLimits() {
        float listHeight = this.getContentHeight() - getHeight();

        if (listHeight < 0) listHeight /= 2;

        if (this.scrollDistance < 0.0F) this.scrollDistance = 0.0F;

        if (this.scrollDistance > listHeight)
            this.scrollDistance = listHeight;
    }

    @Override
    public void onMouseInput(int mouseX, int mouseY) {
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.scrollDistance += (-1 * scroll / 120.0F) * this.generalScrollingDistance / 2;
        }
    }

    public boolean isSelected(int index) {
        return index == getSelectedIndex();
    }

    public void select(int index) {
        if (index >= 0 && index < getEntries().size())
            this.selectedIndex = index;
    }

    public ScrollingList add(int height, FunctionalEntry entry) {
        add(new Entry() {
            @Override
            public int getEntryHeight() {
                return height;
            }

            @Override
            public void onEntryDraw(ScrollingList list, int index, float x, float y, float width) {
                entry.onEntryDraw(list, index, x, y, width);
            }
        });
        return this;
    }

    public interface Entry {
        int getEntryHeight();

        void onEntryDraw(ScrollingList list, int index, float x, float y, float width);
    }

    @FunctionalInterface
    public interface FunctionalEntry {
        void onEntryDraw(ScrollingList list, int index, float x, float y, float width);
    }
}