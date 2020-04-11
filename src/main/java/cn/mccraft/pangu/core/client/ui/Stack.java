package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.client.ui.meta.Alignment;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(chain = true, fluent = true)
public abstract class Stack extends Container {
    protected float wholeWidth, wholeHeight;

    @Getter
    protected float padding = 0;

    @Getter @Setter
    protected Alignment alignment = Alignment.CENTER;

    private boolean firstComponent = true;

    public Stack(Screen screen) {
        super(screen.width, screen.height);
        setScreen(screen);
    }

    @Override
    public Stack addComponent(@Nonnull Component c) {
        super.addComponent(c);
        fixSize(c, firstComponent);
        firstComponent = false;
        resolve();
        return this;
    }

    @Override
    public Stack addComponents(Component... cs) {
        super.addComponents(cs);

        for (Component c : cs) {
            fixSize(c, firstComponent);
            firstComponent = false;
        }

        resolve();
        return this;
    }

    @Override
    public Stack setPosition(float x, float y) {
        super.setPosition(x, y);
        setOffset(0, 0);
        resolve();
        return this;
    }

    public abstract void fixSize(Component c, boolean first);

    public abstract void resolve();

    @Override
    public Component setCenteredPosition(float x, float y) {
        return setPosition(x - wholeWidth / 2, y - wholeHeight / 2);
    }

    @Override
    public float getWidth() {
        return wholeWidth;
    }

    @Override
    public float getHeight() {
        return wholeHeight;
    }

    public Stack padding(float padding) {
        this.padding = padding;
        firstComponent = true;
        wholeHeight = 0;
        wholeWidth = 0;
        for (Component component : getComponents()) {
            fixSize(component, firstComponent);
            firstComponent = false;
        }
        resolve();
        return this;
    }
}
