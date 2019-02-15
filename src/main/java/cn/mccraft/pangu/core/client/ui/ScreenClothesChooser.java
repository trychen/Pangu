package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.builtin.EntityShow;
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

        ScrollingList scrollingList = new ScrollingList(200, 200);
        scrollingList.add(100, (list, index, x, y, width) ->
                Rect.drawGradientTop2Bottom(x + 1, y + 1, x + width - 1, y + 100 - 1, 0xFF7e21d3, 0xFFb886e9));
        scrollingList.add(50, (list, index, x, y, width) ->
                Rect.drawGradientTop2Bottom(x + 1, y + 1, x + width - 1, y + 50 - 1, 0xFF4ae290, 0xFF50c2e3));
        scrollingList.select(0);
        addComponent(scrollingList.setPosition(width / 2, height / 2 - 100));
    }

    @Override
    public void draw() {
    }
}
