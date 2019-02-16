package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.gui.Icon;
import cn.mccraft.pangu.core.client.gui.Style;
import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

public class IconButton extends Button {
    public static final Style NORMAL = Style.of(PG_BUTTONS_TEXTURE, 200, 0, 20);
    public static final Style CONTAINER = Style.of(PG_BUTTONS_TEXTURE, 220, 0, 22);
    public static final Style GREEN = Style.of(PG_BUTTONS_TEXTURE, 220, 66, 22);

    /**
     * The style of button
     */
    @Getter
    @Setter
    protected final Style style;

    /**
     * The texture of the icon
     */
    @Getter
    @Setter
    protected Icon icon;

    public IconButton(int width, int height, Style style, Icon icon) {
        super(width, height);
        this.style = style;
        this.icon = icon;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.bind(style.getTexture());
        int state = this.getHoverState();

        Rect.drawTextured(
                getX(), getY(),
                style.getX(), style.getY() + state * style.getHeight(),
                this.width, this.height);

        // draw icon
        icon.draw(getX(), getY(), style.getWidth());
    }
}
