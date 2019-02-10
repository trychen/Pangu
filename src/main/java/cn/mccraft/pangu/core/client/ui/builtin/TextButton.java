package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.gui.Style;
import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

@SideOnly(Side.CLIENT)
@Accessors(chain = true)
public class TextButton extends Button {
    public static final Style NORMAL = Style.of(PG_BUTTONS_TEXTURE, 0, 0, 200, 20, 10, 0xE0E0E0, 0x6affec, 0xa0a0a0, true);
    public static final Style PRIMARY = Style.of(PG_BUTTONS_TEXTURE, 0, 60, 200, 20, 10, 0xE0E0E0, 0xE0E0E0, 0xa0a0a0, true);
    public static final Style DARK = Style.of(PG_BUTTONS_TEXTURE, 0, 120, 200, 20, 9, 0xDDDDDD, 0xDDDDDD, 0xa0a0a0, true);
    public static final Style WHITE = Style.of(PG_BUTTONS_TEXTURE, 0, 180, 200, 20, 9, 0x121212, 0x121212, 0xa0a0a0, false);

    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    @SideOnly(Side.CLIENT)
    private FontProvider font;

    @Getter
    @Setter
    private Style style;

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
        this.style = style;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.startDrawing();
        Rect.bind(getStyle().getTexture());
        int state = this.getHoverState();

        Rect.drawTextured(
                getX(),
                getY(),
                style.getX(),
                style.getY() + state * style.getHeight(),
                width / 2, height);

        Rect.drawTextured(
                getX() + width / 2,
                getY(),
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
                getX() + this.width / 2 - font.getStringWidth(text) / 2,
                getY() + (this.height - style.getTextOffset()) / 2,
                fontColor,
                style.hasFontShadow());
    }
}
