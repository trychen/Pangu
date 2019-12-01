package cn.mccraft.pangu.core.client.ui.builtin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
public class HorizontalScrollingList<T extends HorizontalScrollingList.Entry> extends HorizontalScrolling {
    @Delegate
    @Getter
    protected List<T> entries = new ArrayList<>();

    @Getter
    @Setter
    protected boolean highlightSelected = true;

    public HorizontalScrollingList(float width, float height) {
        super(width, height);
    }

    @Override
    public void onContentReleased(float mouseListX, float mouseListY) {
        float currentX = 0;
        for (int i = 0; i < getEntryCounts(); i++) {
            T entry = getEntry(i);
            if (mouseListX >= currentX && mouseListX <= (currentX + entry.getEntryWidth(this, i))) {
                entry.onEntityClick(this, i);
                break;
            }
            currentX += entry.getEntryWidth(this, i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onContentDraw(float ticks, float baseX, float mouseListX, float mouseListY) {
        float slotLeft = baseX;
        float currentX = 0;
        for (int index = 0; index < getEntryCounts(); index++) {
            HorizontalScrollingList.Entry entry = getEntry(index);
            float entryWidth = entry.getEntryWidth(this, index);
            if (slotLeft <= getX() + getWidth() && slotLeft + entryWidth >= getX()) {
                entry.onEntryDraw(this, index, slotLeft, getY(), getContentWidth(), mouseListX - currentX, mouseListY);
            }
            currentX += entryWidth;
            slotLeft += entryWidth;
        }
    }

    public float getContentWidth() {
        int total = 0;
        for (int i = 0; i < getEntryCounts(); i++) {
            total += getEntry(i).getEntryWidth(this, i);
        }
        return total;
    }

    public int getEntryCounts() {
        return getEntries().size();
    }

    public T getEntry(int index) {
        return getEntries().get(index);
    }

    public interface Entry {
        /**
         * The height of entry
         */
        float getEntryWidth(HorizontalScrollingList list, int index);

        /**
         * @param list scrolling list
         * @param index the index of this entry
         * @param x entry left
         * @param y entry top
         * @param width the content width of the list
         */
        void onEntryDraw(HorizontalScrollingList list, int index, float x, float y, float width, float contentMouseX, float contentMouseY);

        void onEntityClick(HorizontalScrollingList list, int index);
    }
}