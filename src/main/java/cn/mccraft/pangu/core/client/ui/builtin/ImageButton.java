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
    protected boolean withHoverEffect;

    public ImageButton() {
        super(0F, 0);
    }

    public ImageButton(TextureProvider image, float width, float height) {
        super(width, height);
        this.image = image;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.startDrawing();
        Rect.color(getColor());
        Rect.bind(getImage());

        if (isLinear()) Rect.linearFiltering();
        else Rect.nearestFiltering();

        Rect.drawFullTexTextured(getX(), getY(), getWidth(), getHeight());
        if (isHovered() && isWithHoverEffect()) Rect.drawBox(getX(), getY(), getWidth(), getHeight(), 0x2cFFFFFF);
    }
}
