package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.util.render.CustomFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class TextLabel extends GuiButton {
    private final CustomFont font;

    public TextLabel(int buttonId, int x, int y, String buttonText, CustomFont font) {
        super(buttonId, x, y, font.getStringWidth(buttonText), font.getHeight(),  buttonText);
        this.font = font;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        font.drawString(
                this.displayString,
                this.x,
                this.y + height / 2.0F,
                0xFFFFFFFF,
                hovered);
    }

    public TextLabel setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}
