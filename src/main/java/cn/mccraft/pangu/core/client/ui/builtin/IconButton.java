package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.client.ui.meta.Icon;
import cn.mccraft.pangu.core.client.ui.meta.Style;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;

import static cn.mccraft.pangu.core.client.PGClient.PG_BUTTONS_TEXTURE;

public class IconButton extends Button {
    public static final Style NORMAL = Style.builder().texture(PG_BUTTONS_TEXTURE).x(200).width(20).height(20).build();
    public static final Style CONTAINER = Style.builder().texture(PG_BUTTONS_TEXTURE).x(220).width(22).height(22).build();
    public static final Style GREEN = Style.builder().texture(PG_BUTTONS_TEXTURE).x(220).y(22).width(22).height(22).build();

    /**
     * The style of button
     */
    @Getter
    @Setter
    protected Style style;

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
        Rect.startDrawing();
        Rect.bind(style.texture());
        int state = this.getHoverState();

        Rect.drawTextured(
                getX(), getY(),
                style.x(), style.y() + state * style.height(),
                width, height);

        // draw icon
        icon.drawCentered(getX() + width / 2, getY() + height / 2);
    }
}
