package cn.mccraft.pangu.core.client.ui.builtin;

import cn.mccraft.pangu.core.client.ui.Component;
import cn.mccraft.pangu.core.client.ui.Container;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nonnull;

@Accessors(chain = true)
@Getter
@Setter
public class Waterfall extends Container {
    protected final int column;
    protected float[] currentY;
    protected float paddingX, paddingY, entryWidth;
    protected boolean rectifiedHeight = false;

    public Waterfall(float entryWidth, int column, float paddingX) {
        super(entryWidth * column + paddingX * (column - 1), 0);
        this.column = column;
        this.paddingX = paddingX;
        this.entryWidth = entryWidth;
        this.currentY = new float[column];
    }

    @Override
    public Container addComponent(@Nonnull Component c) {
        int minOffsetYIndex = 0;
        float minOffsetY = currentY[0];

        // 获取最小插入点
        for (int i = 1; i < currentY.length; i++) {
            float currentOffsetY = currentY[i];
            if (currentOffsetY < minOffsetY) {
                minOffsetYIndex = i;
                minOffsetY = currentOffsetY;
            }
        }

        float x = getColumnStartX(minOffsetYIndex);

        c.setPosition(x, minOffsetY);
        super.addComponent(c);

        currentY[minOffsetYIndex] += c.getHeight() + paddingY;

        rectifiedHeight = false;

        return this;
    }

    @Override
    public Container addComponents(Component... cs) {
        for (Component c : cs) {
            addComponent(c);
        }
        return this;
    }

    public float getColumnStartX(int index) {
        return index * entryWidth + index * paddingX;
    }

    @Override
    public void onDraw(float partialTicks, int mouseX, int mouseY) {
        if (!rectifiedHeight) {
            rectifyHeight();
            rectifiedHeight = true;
        }
        super.onDraw(partialTicks, mouseX, mouseY);
    }

    public void rectifyHeight() {
        // 获取最大插入点
        float maxOffsetY = currentY[0];
        for (int i = 1; i < currentY.length; i++) {
            float currentOffsetY = currentY[i];
            if (currentOffsetY > maxOffsetY) {
                maxOffsetY = currentOffsetY;
            }
        }
        setHeight(maxOffsetY);
    }
}
