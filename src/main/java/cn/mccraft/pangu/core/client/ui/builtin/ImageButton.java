package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.util.image.TextureProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;

public class ImageButton extends Button {
    @Getter
    @Setter
    protected TextureProvider image;

    @Getter
    @Setter
    protected int color;

    @Getter
    @Setter
    protected boolean linear = true;

    @Getter
    @Setter
    protected boolean hover;

    public ImageButton() {
        super(0F, 0);
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.startDrawing();
        Rect.color(color);
        Rect.bind(getImage());

        if (isLinear()) Rect.linearFiltering();
        else Rect.nearestFiltering();

        Rect.drawFullTexTextured(getX(), getY(), getWidth(), getHeight());
        if (isHovered() && isHover()) Rect.drawBox(getX(), getY(), getWidth(), getHeight(), 0x2cFFFFFF);
    }
}
