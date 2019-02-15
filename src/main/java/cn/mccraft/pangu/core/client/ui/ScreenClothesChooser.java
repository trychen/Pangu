package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.builtin.EntityShow;
import cn.mccraft.pangu.core.client.ui.builtin.Scrolling;
import cn.mccraft.pangu.core.client.ui.builtin.ScrollingList;
import cn.mccraft.pangu.core.client.ui.builtin.SelectionBox;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class ScreenClothesChooser extends Screen {
    private EntityShow entityShow;

    @BindKeyPress(description = "test.ScreenClothesChooser", keyCode = Keyboard.KEY_K)
    public static void key() {
        new ScreenClothesChooser().open();
    }

    @Override
    public void init() {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        addComponent((entityShow = new EntityShow(Minecraft.getMinecraft().player)).setScale(90).setPosition(halfWidth - 120, halfHeight + 80));
        addComponent(new SelectionBox("展示后背").setColor(0xFFFFFFFF).setSelectEvent((isSelected, mouseButton, mouseX, mouseY) -> {
            entityShow.setShowBack(isSelected);
            return isSelected;
        }).setCenteredPosition(halfWidth - 120, halfHeight + 100));

        GradientScrollingList scrollingList = new GradientScrollingList(50, 200);
        scrollingList.select(0);
        addComponent(scrollingList.setPosition(width / 2, height / 2 - 25));
    }

    @Override
    public void draw() {
    }

    class GradientScrollingList extends ScrollingList implements ScrollingList.Entry {
        public GradientScrollingList(int height, int width) {
            super(height, width);
        }

        @Override
        public int getEntryHeight(ScrollingList list, int index) {
            return index == 0?100:50;
        }

        @Override
        public void onEntryDraw(ScrollingList list, int index, float x, float y, float width) {
            if (index == 0) {
                Rect.drawGradientTop2Bottom(x + 1, y + 1, x + width - 1, y + 100 - 1, 0xFF7e21d3, 0xFFb886e9);
            } else {
                Rect.drawGradientTop2Bottom(x + 1, y + 1, x + width - 1, y + 50 - 1, 0xFF4ae290, 0xFF50c2e3);
            }
        }

        @Override
        public int getEntryCounts() {
            return 2;
        }

        @Override
        public Entry getEntry(int index) {
            return this;
        }
    }
}
