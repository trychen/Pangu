package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.util.font.StringRenderer;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextLabel extends GuiButton {
    private final StringRenderer font;
    private boolean renderBox;
    private int fontYOffset;
    private int fontColor = 0xFFFFFFFF, hoverColor = 0xFFFFFFFF;

    public TextLabel(int buttonId, int x, int y, int height, String buttonText, StringRenderer font) {
        this(buttonId, x, y, height, height / 3, buttonText, font);
    }

    public TextLabel(int buttonId, int x, int y, String buttonText, StringRenderer font) {
        this(buttonId, x, y, font.getSize() / 2, buttonText, font);
    }

    public TextLabel(int buttonId, int x, int y, int height, int fontYOffset, String buttonText, StringRenderer font) {
        super(buttonId, x, y, font.getStringWidth(buttonText), height, buttonText);
        this.font = font;
        this.fontYOffset = fontYOffset;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if (renderBox) Rect.draw(x, y, x + width, y + height, 0xFFBB0000);

        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        font.drawString(
                this.displayString,
                this.x,
                this.y + fontYOffset,
                this.hovered ? hoverColor : fontColor,
                true);
        GlStateManager.disableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
    }

    public TextLabel setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public StringRenderer getFont() {
        return font;
    }

    public boolean isRenderBox() {
        return renderBox;
    }

    public TextLabel setRenderBox(boolean renderBox) {
        this.renderBox = renderBox;
        return this;
    }

    public int getFontYOffset() {
        return fontYOffset;
    }

    public TextLabel setFontYOffset(int fontYOffset) {
        this.fontYOffset = fontYOffset;
        return this;
    }

    public int getFontColor() {
        return fontColor;
    }

    public TextLabel setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public int getHoverColor() {
        return hoverColor;
    }

    public TextLabel setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
        return this;
    }

    public TextLabel setHeight(int height) {
        this.height = height;
        return this;
    }
}
