package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.Ticking;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Accessors(chain = true)
public abstract class HorizontalScrolling extends Component {

    protected float scrollFactor;
    protected float initialMouseClickX = -2.0F;
    protected double scrollVelocity;

    protected Ticking scroller;

    @Getter
    @Setter
    protected int scrollBarHeight = 6;

    @Getter
    protected float scrollDistance;

    @Getter
    @Setter
    protected float generalScrollingDistance = 8;

    @Getter
    @Setter
    protected boolean showScrollBar = true;

    public HorizontalScrolling(float width, float height) {
        setSize(width, height);
        this.scroller = (() -> {
            if (this.scrollVelocity == 0.0 && this.scrollDistance >= 0.0 && this.scrollDistance <= this.getMaxScroll()) {
                this.scroller.unregisterTick();
            }
            else {
                final double change = this.scrollVelocity * 0.3;
                if (this.scrollVelocity != 0.0) {
                    this.scrollDistance += (float)change;
                    this.scrollVelocity -= this.scrollVelocity * ((this.scrollDistance >= 0.0 && this.scrollDistance <= this.getMaxScroll()) ? 0.2 : 0.4);
                    if (Math.abs(this.scrollVelocity) < 0.1) {
                        this.scrollVelocity = 0.0;
                    }
                }
                if (this.scrollDistance < 0.0f && this.scrollVelocity == 0.0) {
                    this.scrollDistance = Math.min(this.scrollDistance + (0.0f - this.scrollDistance) * 0.2f, 0.0f);
                    if (this.scrollDistance > -0.1f && this.scrollDistance < 0.0f) {
                        this.scrollDistance = 0.0f;
                    }
                }
                else if (this.scrollDistance > this.getMaxScroll() && this.scrollVelocity == 0.0) {
                    this.scrollDistance = Math.max(this.scrollDistance - (this.scrollDistance - this.getMaxScroll()) * 0.2f, this.getMaxScroll());
                    if (this.scrollDistance > this.getMaxScroll() && this.scrollDistance < this.getMaxScroll() + 0.1) {
                        this.scrollDistance = this.getMaxScroll();
                    }
                }
            }
        });
    }

    public abstract float getContentWidth();

    public float getContentHeight() {
        if (isShowScrollBar())
            return getHeight() - scrollBarHeight;
        return getHeight();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        drawBackground();

        float scrollBarTop = getY() + getContentHeight();
        float scrollBarBottom = scrollBarTop + scrollBarHeight;

        if (isShowScrollBar() && (getScreen() == null || getScreen().getModal() == null) && Mouse.isButtonDown(0)) {
            if (this.initialMouseClickX == -1.0F) {
                if (isHovered()) {
                    // on scroll bar clicked
                    if (mouseY >= scrollBarTop && mouseX <= scrollBarBottom) {
                        this.scrollFactor = -1.0F;
                        float scrollWidth = this.getContentWidth() - getHeight();
                        if (scrollWidth < 1) scrollWidth = 1;

                        float var13 = (getWidth() * getWidth()) / this.getContentWidth();

                        if (var13 < 32) var13 = 32;
                        if (var13 > getWidth()) var13 = getWidth();

                        this.scrollFactor /= (getWidth() - var13) / scrollWidth;
                    } else {
                        this.scrollFactor = 1.0F;
                    }

                    this.initialMouseClickX = mouseX;
                } else {
                    this.initialMouseClickX = -2.0F;
                }
            } else if (this.initialMouseClickX >= 0.0F) {
                this.scrollDistance -= ((float) mouseX - this.initialMouseClickX) * this.scrollFactor;
                this.initialMouseClickX = (float) mouseX;
            }
        } else {
            this.initialMouseClickX = -1.0F;
        }

        applyScrollLimits();

        Minecraft client = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(client);

        double scaleW = client.displayWidth / res.getScaledWidth_double();
        double scaleH = client.displayHeight / res.getScaledHeight_double();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(
                (int) (getX() * scaleW),     (int) (client.displayHeight - ((getY() + getHeight()) * scaleH)),
                (int) (getWidth() * scaleW), (int) (getHeight() * scaleH)
        );

        float baseX = this.getX() - this.scrollDistance;

        float mouseListX = mouseX - getX() + this.scrollDistance;

        this.onContentDraw(partialTicks, baseX, mouseListX, mouseY - getY());

        // Scrolling bar
        float extraWidth = this.getContentWidth() - getWidth();

        if (isShowScrollBar() && extraWidth > 0) {
            float width = (getWidth() * getWidth()) / this.getContentWidth();

            if (width > getWidth()) width = getWidth();

            if (width < 32) width = 32;

            float barLeft = this.scrollDistance * (getWidth() - width) / extraWidth + getX();
            if (barLeft < getY()) barLeft = getX();

            drawScrollBar(scrollBarTop, scrollBarBottom, width, barLeft);
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void drawScrollBar(float top, float bottom, float barWidth, float barLeft) {
        GlStateManager.disableTexture2D();
        Rect.draw(getX(), top, getX() + getWidth(), bottom, 0xFF000000);
        Rect.draw(barLeft, top, barLeft + barWidth, bottom, 0xFF808080);
        Rect.draw(barLeft, top, barLeft + barWidth - 1, bottom - 1, 0xFFC0C0C0);
        GlStateManager.enableTexture2D();
    }

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        float mouseListY = mouseY - getY();
        if (mouseListY > getContentHeight()) return;

        float mouseListX = mouseX - getX() + this.scrollDistance;

        onContentPressed(mouseButton, mouseListX, mouseListY);
    }


    @Override
    public void onMouseReleased(int mouseX, int mouseY) {
        float mouseListY = mouseY - getY();
        if (mouseListY > getContentHeight()) return;

        float mouseListX = mouseX - getX() + this.scrollDistance;

        onContentReleased(mouseListX, mouseListY);
    }

    private void applyScrollLimits() {
        float listWidth = this.getContentWidth() - getWidth();

        if (listWidth < 0) listWidth /= 2;

        if (this.scrollDistance < 0.0F) this.scrollDistance = 0.0F;

        if (this.scrollDistance > listWidth)
            this.scrollDistance = listWidth;
    }

    public float getMaxScroll() {
        return this.getContentWidth() - getWidth();
    }

    @Override
    public void onMouseInput(int mouseX, int mouseY) {
        int i2 = Mouse.getEventDWheel();
        if (i2 != 0) {
            if (i2 > 0) {
                i2 = 1;
            }
            else if (i2 < 0) {
                i2 = -1;
            }
            if (this.scrollDistance <= getMaxScroll() && i2 < 0.0) {
                this.scrollVelocity += 16.0;
            }
            if (this.scrollDistance >= 0.0 && i2 > 0.0) {
                this.scrollVelocity -= 16.0;
            }
            if (!this.scroller.isRegistered()) {
                this.scroller.registerTick();
            }
        }

        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            this.scrollDistance += (-1 * scroll / 120.0F) * this.generalScrollingDistance / 2;
        }
    }

    /**
     * @deprecated {@link HorizontalScrolling#onContentPressed(int, float, float)}
     */
    @Deprecated
    public void onContentClick(float mouseListX, float mouseListY) {
    }

    public void onContentPressed(int mouseButton, float mouseListX, float mouseListY) {
        onContentClick(mouseListX, mouseListY);
    }

    public void onContentReleased(float mouseListX, float mouseListY) {

    }

    public abstract void onContentDraw(float partialTicks, float baseX, float mouseListX, float mouseListY);

    public void drawBackground() {
    }
}