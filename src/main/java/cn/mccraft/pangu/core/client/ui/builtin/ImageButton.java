package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Button;
import cn.mccraft.pangu.core.util.image.TextureProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;

public class ImageButton extends Button {
    @Getter @Setter
    protected TextureProvider texture;
    @Getter @Setter
    protected float u, v;

    public ImageButton(TextureProvider texture, float width, float height, float u, float v) {
        super(width, height);
        this.texture = texture;
        this.u = u;
        this.v = v;
    }


    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.startDrawing();
        Rect.bind(texture);
        int state = this.getHoverState();

        Rect.drawTextured(
                getX(), getY(),
                u, v + state * width,
                width, height);
    }
}
