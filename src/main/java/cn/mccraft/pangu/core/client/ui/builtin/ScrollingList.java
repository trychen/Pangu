package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
public class ScrollingList extends Scrolling {
    @Delegate
    @Getter
    protected List<Entry> entries = new ArrayList<>();

    @Getter
    @Setter
    protected boolean highlightSelected = true;

    @Getter
    protected int selectedIndex = -1;

    public ScrollingList(int height, int width) {
        super(height, width);
    }

    @Override
    public void onContentClick(float mouseListX, float mouseListY) {
        float currentY = 0;
        for (int i = 0; i < getEntryCounts(); i++) {
            Entry entry = getEntry(i);
            if (mouseListY >= currentY && mouseListY <= (currentY + entry.getEntryHeight(this, i))) {
                select(i);
                break;
            }
            currentY += entry.getEntryHeight(this, i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onContentDraw(float baseY) {
        float slotTop = baseY;
        for (int index = 0; index < getEntryCounts(); index++) {
            Entry entry = getEntry(index);

            if (slotTop <= getY() + getHeight() && slotTop + entry.getEntryHeight(this, index) >= getY()) {
                if (this.highlightSelected && this.isSelected(index)) {
                    Rect.drawFrame(getX(), slotTop, getX() + getContentWidth(), slotTop + entry.getEntryHeight(this, index), 1, 0xFF808080);
                }
                entry.onEntryDraw(this, index, getX(), slotTop, getContentWidth());
            }

            slotTop += entry.getEntryHeight(this, index);
        }
    }


    public boolean isSelected(int index) {
        return index == getSelectedIndex();
    }

    public void select(int index) {
        if (isSelected(index)) return;
        if (index < -1 || index >= getEntryCounts()) throw new IndexOutOfBoundsException();
        if (index != -1 && !getEntry(index).onEntryStateChanged(this, index,true)) return;
        for (int i = 0; i < getEntryCounts(); i++) {
            if (i != index && !getEntry(i).onEntryStateChanged(this, i, i == index))
                return;
        }
        this.selectedIndex = index;
    }

    public int getContentHeight() {
        int total = 0;
        for (int i = 0; i < getEntryCounts(); i++) {
            total += getEntry(i).getEntryHeight(this, i);
        }
        return total;
    }

    public int getEntryCounts() {
        return getEntries().size();
    }

    public Entry getEntry(int index) {
        return getEntries().get(index);
    }

    public interface Entry {
        /**
         * The height of entry
         */
        int getEntryHeight(ScrollingList list, int index);

        /**
         * @param list scrolling list
         * @param index the index of this entry
         * @param x entry left
         * @param y entry top
         * @param width the content width of the list
         */
        void onEntryDraw(ScrollingList list, int index, float x, float y, float width);

        /**
         * @param isSelected true if this entry selected
         * @return if allow changing the index
         */
        default boolean onEntryStateChanged(ScrollingList list, int index, boolean isSelected) {
            return true;
        }
    }
}