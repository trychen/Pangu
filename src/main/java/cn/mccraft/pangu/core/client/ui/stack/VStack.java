package cn.mccraft.pangu.core.client.ui.stack;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.Stack;
import cn.mccraft.pangu.core.client.ui.meta.Alignment;

public class VStack extends Stack {
    public VStack(Screen screen) {
        super(screen);
    }

    public VStack(float width, float height) {
        super(width, height);
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
        float nowX = getX() + (alignment() == Alignment.CENTER ? wholeWidth / 2 : 0);
        float nowY = getY();
        for (Component component : getComponents()) {
            component.setPosition(nowX, nowY);
            if (alignment() == Alignment.CENTER) {
                component.setPosition(nowX - component.getWidth() / 2, nowY);
            } else if (alignment() == Alignment.LEADING) {
                component.setPosition(nowX, nowY);
            } else if (alignment() == Alignment.ENDING) {
                component.setPosition(nowX + wholeWidth - component.getWidth(), nowY);
            }

            nowY += padding + component.getHeight();
        }
    }
}
