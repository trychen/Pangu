package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Container;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(chain = true)
public class LiteSideBarHorizontalScrollingContainer extends HorizontalScrollingContainer {
    @Getter
    @Setter
    protected int color = 0x3c000000;

    public LiteSideBarHorizontalScrollingContainer(@Nonnull Container container, float width) {
        super(container, width);
        this.setScrollBarHeight(12);
        this.setSize(this.getWidth(), this.getHeight() + this.getScrollBarHeight());
    }

    @Override
    public void drawScrollBar(float top, float bottom, float barWidth, float barLeft) {
        Rect.drawBox(barLeft, top + 3, barWidth, 2, color);
    }
}
