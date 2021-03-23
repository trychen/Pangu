package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.Ticking;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@Accessors(chain = true)
public abstract class Scrolling extends Component {
    protected float scrollFactor;
    protected float initialMouseClickY = -2.0F;
    protected double scrollVelocity;

    protected Ticking scroller;

    @Getter
    @Setter
    protected int scrollBarWidth = 6;

    @Getter
    protected float scrollDistance;

    @Getter
    @Setter
    protected float generalScrollingDistance = 8;

    @Getter
    @Setter
    protected boolean showScrollBar = true;

    @Getter
    @Setter
    protected boolean canScrollOnContent = true;

    public Scrolling(float width, float height) {
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

    public abstract float getContentHeight();

    public float getContentWidth() {
        if (isShowScrollBar())
            return getWidth() - scrollBarWidth;
        return getWidth();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        drawBackground();

        float scrollBarLeft = getX() + getContentWidth();
        float scrollBarRight = scrollBarLeft + scrollBarWidth;

        float mouseListY = mouseY - getY() + this.scrollDistance;

        if ((getScreen() == null || getScreen().getModal() == null) && Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovered()) {
                    // on scroll bar clicked
                    if (isShowScrollBar() && mouseX >= scrollBarLeft && mouseX <= scrollBarRight) {
                        this.scrollFactor = -1.0F;
                        float scrollHeight = this.getContentHeight() - getHeight();
                        if (scrollHeight < 1) scrollHeight = 1;

                        float var13 = (getHeight() * getHeight()) / this.getContentHeight();

                        if (var13 < 32) var13 = 32;
                        if (var13 > getHeight()) var13 = getHeight();

                        this.scrollFactor /= (getHeight() - var13) / scrollHeight;
                        this.initialMouseClickY = mouseY;
                    } else {
                        this.scrollFactor = 1.0F;
                        if (isCanScrollOnContent()) this.initialMouseClickY = mouseY;
                    }

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

        this.onContentDraw(partialTicks, baseY, mouseX - getX(), mouseListY);

        // Scrolling bar
        float extraHeight = this.getContentHeight() - getHeight();

        if (isShowScrollBar() && extraHeight > 0) {
            float height = (getHeight() * getHeight()) / this.getContentHeight();

            if (height > getHeight()) height = getHeight();

            if (height < 32) height = 32;

            float barTop = this.scrollDistance * (getHeight() - height) / extraHeight + getY();
            if (barTop < getY()) barTop = getY();

            drawScrollBar(scrollBarLeft, scrollBarRight, height, barTop);
        }

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void drawScrollBar(float left, float right, float barHeight, float barTop) {
        GlStateManager.disableTexture2D();
        Rect.draw(left, getY(), right, getY() + getHeight(), 0xFF000000);
        Rect.draw(left, barTop, right, barTop + barHeight, 0xFF808080);
        Rect.draw(left, barTop, right - 1, barTop + barHeight - 1, 0xFFC0C0C0);
        GlStateManager.enableTexture2D();
    }

    private void applyScrollLimits() {
        float listHeight = this.getContentHeight() - getHeight();

        if (listHeight < 0) listHeight /= 2;

        if (this.scrollDistance < 0.0F) this.scrollDistance = 0.0F;

        if (this.scrollDistance > listHeight)
            this.scrollDistance = listHeight;
    }

    public float getMaxScroll() {
        return this.getContentHeight() - getHeight();
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

    @Override
    public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
        float mouseListX = mouseX - getX();
        if (mouseListX > getContentWidth()) return;

        float mouseListY = mouseY - getY() + this.scrollDistance;

        onContentPressed(mouseButton, mouseListX, mouseListY);
    }

    @Override
    public void onMouseReleased(int mouseButton, int mouseX, int mouseY) {
        float mouseListX = mouseX - getX();
        if (mouseListX > getContentWidth()) return;

        float mouseListY = mouseY - getY() + this.scrollDistance;

        onContentReleased(mouseButton, mouseListX, mouseListY);
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

    public void onContentReleased(int mouseButton, float mouseListX, float mouseListY) {
    }

    public abstract void onContentDraw(float ticks, float baseY, float mouseListX, float mouseListY);

    public void drawBackground() {
    }
}