package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Container;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import lombok.Getter;
import lombok.Setter;

public class ScrollingContainer extends Scrolling {
    @Getter
    @Setter
    protected Container container;

    public ScrollingContainer(Container container, float width) {
        super(width, container.getHeight());
        this.container = container;
    }

    @Override
    public float getContentHeight() {
        return container.getHeight();
    }

    @Override
    public void onContentClick(float mouseListX, float mouseListY) {
        container.onMousePressed(0, (int) (mouseListX), (int) (mouseListY));
        container.onMouseReleased((int) (mouseListX), (int) (mouseListY));
    }

    @Override
    public void onContentDraw(float ticks, float baseY, float mouseListX, float mouseListY) {
        container.setOffset(getX(), baseY);
        container.onDraw(ticks, (int) (mouseListX + getX()), (int) (baseY + mouseListY));
    }
}
