package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Container;
import lombok.Getter;
import lombok.Setter;

public class HorizontalScrollingContainer extends HorizontalScrolling {
    @Getter
    @Setter
    protected Container container;

    public HorizontalScrollingContainer(Container container, float width) {
        super(width, container.getHeight());
        this.container = container;
    }

    @Override
    public float getContentWidth() {
        return container.getWidth();
    }

    @Override
    public void onContentClick(float mouseListX, float mouseListY) {
        container.onMousePressed(0, (int) (mouseListX), (int) (mouseListY));
        container.onMouseReleased((int) (mouseListX), (int) (mouseListY));
    }

    @Override
    public void onContentDraw(float ticks, float baseX, float mouseListX, float mouseListY) {
        container.setOffset(baseX, getY());
        container.onDraw(ticks, (int) (mouseListX + baseX), (int) (getY() + mouseListY));
    }
}