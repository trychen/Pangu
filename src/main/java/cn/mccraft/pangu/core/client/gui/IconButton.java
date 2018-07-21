package cn.mccraft.pangu.core.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import static cn.mccraft.pangu.core.client.ClientProxy.PG_BUTTONS_TEXTURE;

public class IconButton extends GuiButton {
    private final ResourceLocation icon;
    private final int size, offsetX, offsetY;
    private final Style style;

    public IconButton(int buttonId, int x, int y, ResourceLocation texture, int offsetX, int offsetY, int size) {
        this(buttonId, x, y, texture, offsetX, offsetY, size, Style.NORMAL);
    }

    public IconButton(int buttonId, int x, int y, ResourceLocation texture, int size) {
        this(buttonId, x, y, texture, 0, 0, size, Style.NORMAL);
    }

    public IconButton(int buttonId, int x, int y, ResourceLocation texture, int size, Style style) {
        this(buttonId, x, y, texture, 0, 0, size, style);
    }

    public IconButton(int buttonId, int x, int y, ResourceLocation icon, int offsetX, int offsetY, int size, Style style) {
        super(buttonId, x, y, style.size, style.size, "");
        if (style.size < size) throw new IllegalArgumentException("Unable to create a IconButton that icon size bigger than style size " + style.size);
        this.icon = icon;
        this.size = size;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.style = style;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        GlStateManager.pushMatrix();
        minecraft.getTextureManager().bindTexture(style.texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int state = this.getHoverState(this.hovered);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        this.drawTexturedModalRect(
                this.x, this.y,
                style.x, style.y + state * style.size,
                this.width, this.height);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        minecraft.getTextureManager().bindTexture(icon);
        this.drawTexturedModalRect(
                this.x + (float) (style.size - size) / 2, this.y + (float) (style.size - size) / 2,
                offsetX, offsetY,
                size, size);
        GlStateManager.popMatrix();

        this.mouseDragged(minecraft, mouseX, mouseY);
    }

    public IconButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public enum Style {
        NORMAL(PG_BUTTONS_TEXTURE, 200, 0, 20),
        CONTAINER(PG_BUTTONS_TEXTURE, 220, 0, 22);

        private final ResourceLocation texture;
        private final int x, y, size;

        Style(ResourceLocation texture, int x, int y, int size) {
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.size = size;
        }
    }
}
