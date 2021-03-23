package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorPane extends Component {
    protected int color;

    public ColorPane(int color) {
        this.color = color;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        Rect.drawBox(getX(), getY(), getWidth(), getHeight(), color);
    }
}
