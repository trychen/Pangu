package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.render.Rect;

import java.util.List;

public class StringScrollingList extends ScrollingList implements ScrollingList.Entry {
    protected List<String> data;

    public StringScrollingList(int width, int height, List<String> data) {
        super(width, height);
        this.data = data;
    }

    @Override
    public int getEntryHeight(ScrollingList list, int index) {
        return 13;
    }

    @Override
    public void onEntryDraw(ScrollingList list, int index, float x, float y, float width) {
        Rect.drawGradientTop2Bottom(x + 1, y + 1, x + width - 1, y + getEntryHeight(list, index) - 1, 0x204ae290, 0x2050c2e3);
        DefaultFontProvider.INSTANCE.drawCenteredString(data.get(index), x + getContentWidth() / 2, y + 2, 0xFFFFFF, true);
    }

    @Override
    public int getEntryCounts() {
        return data.size();
    }

    @Override
    public Entry getEntry(int index) {
        return this;
    }
}
