package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Container;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.TransformHover;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nonnull;

public class HorizontalScrollingContainer extends HorizontalScrolling implements TransformHover {
    @Getter
    protected Container container;

    public HorizontalScrollingContainer(@Nonnull Container container, float width) {
        super(width, container.getHeight());
        this.container = container;
        this.container.setParent(this);
    }

    public void setContainer(Container container) {
        container.setScreen(getScreen());
        container.setParent(this);
        this.container = container;
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
    public void onContentReleased(int mouseButton, float mouseListX, float mouseListY) {
        container.onMouseReleased(mouseButton, (int) (mouseListX), (int) (mouseListY));
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
