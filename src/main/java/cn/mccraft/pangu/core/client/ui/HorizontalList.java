package cn.mccraft.pangu.core.client.ui;

import javax.annotation.Nonnull;

public class HorizontalList extends Container {
    private float padding = 10;

    public HorizontalList(int width, int height) {
        super(width, height);
    }

    @Override
    public HorizontalList addComponent(@Nonnull Component c) {
        super.addComponent(c);
        calPosition();
        return this;
    }

    @Override
    public HorizontalList addComponents(Component... cs) {
        super.addComponents(cs);
        calPosition();
        return this;
    }

    @Override
    public HorizontalList setPosition(float x, float y) {
        super.setPosition(x, y);
        calPosition();
        return this;
    }

    public void calPosition() {
        if (getComponents().size() == 0) return;

        float wholeWidth = 0;
        float wholeHeight = 0;
        for (Component component : getComponents()) {
            wholeWidth += component.getWidth();
            wholeHeight += component.getHeight();
        }

        float nowX = (width - wholeWidth) / 2;
        float nowY = y + wholeHeight / 2;

        for (Component component : getComponents()) {
            component.setPosition(nowX, nowY - component.getHeight() / 2);
            nowX += padding + component.getWidth();
        }
    }

    public float getPadding() {
        return padding;
    }

    public HorizontalList setPadding(float padding) {
        this.padding = padding;
        return this;
    }
}