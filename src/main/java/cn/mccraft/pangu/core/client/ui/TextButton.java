package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.gui.Style;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.renderer.GlStateManager;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

public class TextButton extends Button {
    public static final Style NORMAL = Style.of(PG_BUTTONS_TEXTURE, 0, 0, 200, 20, 10, 0xE0E0E0, 0x6affec, 0xa0a0a0, true);
    public static final Style PRIMARY = Style.of(PG_BUTTONS_TEXTURE, 0, 60, 200, 20, 10, 0xE0E0E0, 0xE0E0E0, 0xa0a0a0, true);
    public static final Style DARK = Style.of(PG_BUTTONS_TEXTURE, 0, 120, 200, 20, 9, 0xDDDDDD, 0xDDDDDD, 0xa0a0a0, true);
    public static final Style WHITE = Style.of(PG_BUTTONS_TEXTURE, 0, 180, 200, 20, 9, 0x121212, 0x121212, 0xa0a0a0, false);

    private String text;
    private FontProvider font;
    private Style style = NORMAL;

    public TextButton(String text) {
        this(text, DefaultFontProvider.INSTANCE, NORMAL);
    }

    public TextButton(String text, Style style) {
        this(text, DefaultFontProvider.INSTANCE, style);
    }

    public TextButton(String text, FontProvider font, Style style) {
        super(font.getStringWidth(text) + 20, style.getHeight());
        this.text = text;
        this.font = font;
    }

    @Override
    public void onClick(int mouseButton, int mouseX, int mouseY) { }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(getStyle().getTexture());
        int state = this.getHoverState();

        Rect.drawTextured(
                x,
                y,
                style.getX(),
                style.getY() + state * style.getHeight(),
                width / 2, height);

        Rect.drawTextured(
                x + width / 2,
                y,
                style.getWidth() - this.width / 2,
                style.getY() + state * style.getHeight(),
                width / 2, height);

        int fontColor = style.getFontColor();

        if (this.isDisabled()) {
            fontColor = style.getDisabledFontColor();
        } else if (this.isHovered()) {
            fontColor = style.getHoverFontColor();
        }

        font.drawString(
                text,
                this.x + this.width / 2 - font.getStringWidth(text) / 2,
                this.y + (float) (this.height - style.getTextOffset()) / 2,
                fontColor,
                style.hasFontShadow());

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableTexture2D();
    }

    public String getText() {
        return text;
    }

    public FontProvider getFont() {
        return font;
    }

    public TextButton setText(String text) {
        this.text = text;
        return this;
    }

    public TextButton setFont(FontProvider font) {
        this.font = font;
        return this;
    }

    public Style getStyle() {
        return style;
    }

    public TextButton setStyle(Style style) {
        this.style = style;
        return this;
    }
}
