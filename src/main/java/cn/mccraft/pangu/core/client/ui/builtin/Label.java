package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.font.FontProvider;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(chain = true)
public class Label extends Button {
    @Getter
    @Setter
    private String text;

    @Getter
    @Setter
    private FontProvider fontProvider;

    @Setter
    @Getter
    private int color = 0xFFFFFFFF, hoverColor;

    @Setter
    @Getter
    private boolean dropShadow = true;

    @Setter
    @Getter
    private boolean centered = false;

    public Label(String text) {
        this(text, DefaultFontProvider.INSTANCE);
    }

    public Label(@Nonnull String text, @Nonnull FontProvider fontProvider) {
        setSize(fontProvider.getStringWidth(text), fontProvider.getFontHeight());
        this.text = text;
        this.fontProvider = fontProvider;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        fontProvider.drawString(text, getX(), getY(), isHovered() && hoverColor != 0 ? hoverColor : color, dropShadow, centered);
    }
}