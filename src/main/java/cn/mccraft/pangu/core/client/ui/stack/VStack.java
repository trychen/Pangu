package cn.mccraft.pangu.core.client.ui.stack;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.Stack;

public class VStack extends Stack {
    public VStack(Screen screen) {
        super(screen);
    }

    @Override
    public void fixSize(Component c, boolean first) {
        if (!first) wholeHeight += padding;
        wholeHeight += c.getHeight();

        if (c.getWidth() > wholeWidth) {
            wholeWidth = c.getWidth();
        }
    }

    @Override
    public void resolve() {
        float nowX = getX();
        float nowY = getY();
        for (Component component : getComponents()) {
            component.setPosition(nowX, nowY);
            nowY += padding + component.getHeight();
        }
    }
}
