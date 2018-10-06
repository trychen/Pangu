package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

public class IconButton extends GuiButton {

    public static final Style NORMAL = Style.of(PG_BUTTONS_TEXTURE, 200, 0, 20);
    public static final Style CONTAINER = Style.of(PG_BUTTONS_TEXTURE, 220, 0, 22);

    /**
     * The style of button
     */
    private final Style style;

    /**
     * The texture of the icon
     */
    private Icon icon;

    public IconButton(int buttonId, int x, int y, Icon icon) {
        this(buttonId, x, y, NORMAL, icon);
    }

    public IconButton(int buttonId, int x, int y, @Nonnull Style style, @Nonnull Icon icon) {
        super(buttonId, x, y, style.getWidth(), style.getHeight(), "");
        this.icon = icon;
        this.style = style;
        if (style.getWidth() < icon.getSize())
            PanguCore.getLogger().error("Unable to create a IconButton that icon size bigger than style size " + style.getWidth(), new IllegalArgumentException());
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(@Nonnull Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        // draw button style
        GlStateManager.pushMatrix();
        minecraft.getTextureManager().bindTexture(style.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int state = this.getHoverState(this.hovered);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.drawTexturedModalRect(
                this.x, this.y,
                style.getX(), style.getY() + state * style.getWidth(),
                this.width, this.height);
        GlStateManager.popMatrix();

        // draw icon
        minecraft.getTextureManager().bindTexture(icon.getTexture());
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(
                this.x + (float) (style.getWidth() - icon.getSize()) / 2, this.y + (float) (style.getWidth() - icon.getSize()) / 2,
                icon.getOffsetX(), icon.getOffsetY(),
                icon.getSize(), icon.getSize());

        this.mouseDragged(minecraft, mouseX, mouseY);
    }

    public IconButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Icon getIcon() {
        return icon;
    }

    public Style getStyle() {
        return style;
    }

    public IconButton setIcon(@Nonnull Icon icon) {
        this.icon = icon;
        return this;
    }
}
