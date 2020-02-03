package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Container;
import cn.mccraft.pangu.core.client.ui.Screen;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;

public class HorizontalScrollingContainer extends HorizontalScrolling {
    @Getter
    @Setter
    protected Container container;

    public HorizontalScrollingContainer(@Nonnull Container container, float width) {
        super(width, container.getHeight());
        this.container = container;
        this.container.setParent(this);
    }

    @Override
    public float getContentWidth() {
        return container.getWidth();
    }

    @Override
    public void onContentPressed(int mouseButton, float mouseListX, float mouseListY) {
        container.onMousePressed(mouseButton, (int) (mouseListX), (int) (mouseListY));
    }

    @Override
    public void onContentReleased(float mouseListX, float mouseListY) {
        container.onMouseReleased((int) (mouseListX), (int) (mouseListY));
    }

    @Override
    public void onContentDraw(float ticks, float baseX, float mouseListX, float mouseListY) {
        container.setOffset(baseX, getY());
        container.onDraw(ticks, (int) (mouseListX + baseX), (int) (getY() + mouseListY));
    }

    @Override
    public Component setScreen(Screen screen) {
        container.setScreen(screen);
        return super.setScreen(screen);
    }
}
