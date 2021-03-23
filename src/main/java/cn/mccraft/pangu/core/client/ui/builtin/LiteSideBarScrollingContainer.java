package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Container;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(chain = true)
public class LiteSideBarScrollingContainer extends ScrollingContainer {
    @Getter
    @Setter
    protected int color = 0x3c000000;

    public LiteSideBarScrollingContainer(@Nonnull Container container, float height) {
        super(container, height);
        this.setScrollBarWidth(6);
        this.setSize(this.getWidth() + getScrollBarWidth(), this.getHeight());
    }

    @Override
    public void drawScrollBar(float left, float right, float barHeight, float barTop) {
        Rect.drawBox(left + 3, barTop, 2, barHeight, color);
    }

    @Override
    public void onContentPressed(int mouseButton, float mouseListX, float mouseListY) {
        container.onMousePressed(mouseButton, (int) (mouseListX + getX()), (int) (mouseListY));
    }

    @Override
    public void onContentReleased(int mouseButton, float mouseListX, float mouseListY) {
        container.onMouseReleased(mouseButton, (int) (mouseListX + getX()), (int) (mouseListY));
    }
}
