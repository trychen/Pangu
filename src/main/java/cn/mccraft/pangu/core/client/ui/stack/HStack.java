package cn.mccraft.pangu.core.client.ui.stack;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.Stack;
import cn.mccraft.pangu.core.client.ui.meta.Alignment;

public class HStack extends Stack {
    public HStack(Screen screen) {
        super(screen);
    }

    @Override
    public void fixSize(Component c, boolean first) {
        if (!first) wholeWidth += padding;
        wholeWidth += c.getWidth();

        if (c.getHeight() > wholeHeight) {
            wholeHeight = c.getHeight();
        }
    }

    @Override
    public void resolve() {
        float nowX = getX();
        float nowY = getY() + (alignment() == Alignment.CENTER ? wholeHeight / 2 : 0);
        for (Component component : getComponents()) {
            if (alignment() == Alignment.CENTER) {
                component.setPosition(nowX, nowY - component.getHeight() / 2);
            } else if (alignment() == Alignment.LEADING) {
                component.setPosition(nowX, nowY);
            } else if (alignment() == Alignment.ENDING) {
                component.setPosition(nowX, nowY + wholeHeight - component.getHeight());
            }
            nowX += padding + component.getWidth();
        }
    }
}
