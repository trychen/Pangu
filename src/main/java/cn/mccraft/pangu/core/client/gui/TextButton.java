package cn.mccraft.pangu.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

/**
 * An GuiButton with custom texture
 */
@SideOnly(Side.CLIENT)
public class TextButton extends GuiButton {
    private final static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    public static final Style NORMAL = Style.of(PG_BUTTONS_TEXTURE, 0, 0, 200, 20, 10, 0xE0E0E0, 0x6affec, 0xa0a0a0, true);
    public static final Style PRIMARY = Style.of(PG_BUTTONS_TEXTURE, 0, 60, 200, 20, 10, 0xE0E0E0, 0xE0E0E0, 0xa0a0a0, true);
    public static final Style DARK = Style.of(PG_BUTTONS_TEXTURE, 0, 120, 200, 20, 9, 0xDDDDDD, 0xDDDDDD, 0xa0a0a0, true);
    public static final Style WHITE = Style.of(PG_BUTTONS_TEXTURE, 0, 180, 200, 20, 9, 0x121212, 0x121212, 0xa0a0a0, false);

    /**
     * left-align if false
     */
    protected boolean textCenterAlign = true;

    protected Style style;

    public TextButton(int buttonId, int x, int y, String buttonText) {
        this(buttonId, x, y, fontRenderer.getStringWidth(buttonText) + 20, 20, buttonText, NORMAL);
    }

    public TextButton(int buttonId, int x, int y, String buttonText, Style style) {
        this(buttonId, x, y, fontRenderer.getStringWidth(buttonText) + 20, 20, buttonText, style);
    }

    public TextButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this(buttonId, x, y, widthIn, heightIn, buttonText, NORMAL);
    }

    public TextButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Style style) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.style = style;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        mc.getTextureManager().bindTexture(style.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int state = this.getHoverState(this.hovered);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.drawTexturedModalRect(
                this.x, this.y,
                style.getX(), style.getY() + state * style.getHeight(),
                this.width, this.height);

        this.drawTexturedModalRect(this.x + this.width / 2,
                this.y, style.getX() - this.width / 2,
                style.getY() + state * style.getHeight(), this.width / 2, this.height);

        this.mouseDragged(mc, mouseX, mouseY);
        int fontColor = style.getFontColor();

        if (!this.enabled) {
            fontColor = style.getDisabledFontColor();
        } else if (this.hovered) {
            fontColor = style.getHoverFontColor();
        }

        fontRenderer.drawString(
                this.displayString,
                isTextCenterAlign() ? (float) (this.x + this.width / 2 - fontRenderer.getStringWidth(displayString) / 2) : (float) this.x + 10,
                this.y + (float) (this.height - style.getTextOffset()) / 2,
                fontColor,
                style.hasFontShadow());
    }

    public boolean isTextCenterAlign() {
        return textCenterAlign;
    }

    public TextButton setTextCenterAlign(boolean textCenterAlign) {
        this.textCenterAlign = textCenterAlign;
        return this;
    }

    public TextButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
