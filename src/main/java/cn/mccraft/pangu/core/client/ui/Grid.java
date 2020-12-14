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
    protected boolean leadingPadding = true;
    protected float minHeight;

    public Grid(float width, float height) {
        super(width, height);
    }

    @Override
    public Container addComponent(@Nonnull Component c) {
        float fixedPaddingX = leadingPadding ? paddingX : (currentX == 0 ? 0 : paddingX);
        if (c.getWidth() + fixedPaddingX > width) {
            if (line != 0) {
                nextLine();
            }
            c.setPosition(currentX, currentY);
            super.addComponent(c);
            nextLine();
            return this;
        }

        if (currentX + c.getWidth() + fixedPaddingX > width) {
            nextLine();
            fixedPaddingX = leadingPadding ? paddingX : (currentX == 0 ? 0 : paddingX);
        }

        if (c.getHeight() > currentLineHeight) {
            currentLineHeight = c.getHeight();
        }
        c.setPosition(currentX + fixedPaddingX, currentY);
        super.addComponent(c);

        currentX += fixedPaddingX + c.getWidth();

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
        if (getHeight() < minHeight) {
            setHeight(minHeight);
        }
    }
}
