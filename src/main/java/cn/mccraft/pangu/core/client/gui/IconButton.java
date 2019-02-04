package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

@SideOnly(Side.CLIENT)
public class IconButton extends GuiButton {

    public static final Style NORMAL = Style.of(PG_BUTTONS_TEXTURE, 200, 0, 20);
    public static final Style CONTAINER = Style.of(PG_BUTTONS_TEXTURE, 220, 0, 22);
    public static final Style GREEN = Style.of(PG_BUTTONS_TEXTURE, 220, 66, 22);

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
//        if (style.getWidth() < icon.getSize())
//            PanguCore.getLogger().error("Unable to create a IconButton that icon size bigger than style size " + style.getWidth(), new IllegalArgumentException());
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(@Nonnull Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;

        // draw button style
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        minecraft.getTextureManager().bindTexture(style.getTexture());
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int state = this.getHoverState(this.hovered);

        Rect.drawTextured(
                this.x, this.y,
                style.getX(), style.getY() + state * style.getHeight(),
                this.width, this.height);

        // draw icon
        icon.draw(x, y, style.getWidth());
    }

    public IconButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Icon getIcon() {
        return icon;
    }

    public IconButton setIcon(@Nonnull Icon icon) {
        this.icon = icon;
        return this;
    }

    public Style getStyle() {
        return style;
    }
}
