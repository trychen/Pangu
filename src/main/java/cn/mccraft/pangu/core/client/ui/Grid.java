package cn.mccraft.pangu.core.client.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(chain = true)
@Getter
@Setter
public class Grid extends Container {
    protected float currentX, currentY;
    protected float paddingX, paddingY;
    protected float currentLineHeight;
    protected int line = 0, lineIndex = 0;

    public Grid(float width, float height) {
        super(width, height);
    }

    @Override
    public Container addComponent(@Nonnull Component c) {
        if (c.getWidth() + paddingX > width) {
            if (line != 0) nextLine();
            c.setPosition(currentX, currentY);
            super.addComponent(c);
            nextLine();
            return this;
        }

        if (currentX + c.getWidth() + paddingX > width) nextLine();

        if (c.getHeight() > currentLineHeight) {
            currentLineHeight = c.getHeight();
        }
        c.setPosition(currentX + paddingX, currentY);
        super.addComponent(c);

        currentX += paddingX + c.getWidth();

        return this;
    }

    @Override
    public Container addComponents(Component... cs) {
        for (Component c : cs) {
            addComponent(c);
        }
        return this;
    }

    public void nextLine() {
        currentY += currentLineHeight + paddingY;
        currentX = 0;
        currentLineHeight = 0;
        line++;
    }

    public void rectifyHeight() {
        setHeight(currentY + currentLineHeight);
    }
}
