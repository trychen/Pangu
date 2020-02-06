package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.builtin.Label;
import cn.mccraft.pangu.core.client.ui.builtin.ScrollingContainer;
import cn.mccraft.pangu.core.util.render.Rect;
import org.lwjgl.input.Keyboard;

public class ScreenTest extends Screen {

    @BindKeyPress(Keyboard.KEY_O)
    public static void open1() {
        new ScreenTest().open();
    }

    @Override
    public void init() {
        Container c = new Container(100, 500){
            @Override
            public void drawBackground() {
                Rect.drawGradientBox(getX(), getY(), getWidth(), getHeight(), 0xFF000000, 0xFF000000, 0xFFFFFFFF, 0xFFFFFFFF);
            }
        };
        c.addComponent(new Label("Test").setColor(0xFFFFFFFF));
        addComponent(new ScrollingContainer(c, 100));
    }
}
