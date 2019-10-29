package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.ui.builtin.HorizontalScrollingContainer;
import cn.mccraft.pangu.core.client.ui.builtin.TextButton;
import org.lwjgl.input.Keyboard;

@DevOnly
public class ScreenTest extends Screen {
    @BindKeyPress(Keyboard.KEY_N)
    public static void key() {
        new ScreenTest().open();
    }

    @Override
    public void init() {
        Container container = new Container(200, 100);
        container.addComponent(new TextButton("Hello World!"));
        container.addComponent(new TextButton("I'm Trychen").setPosition(60, 50));
        addComponent(new HorizontalScrollingContainer(container, 100).setShowScrollBar(true));
    }
}
