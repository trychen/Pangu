package cn.mccraft.pangu.core.client.ui;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public abstract class Component {
    protected Screen screen;
    protected int x, y, width, height;

    /**
     * Drawing this component
     * @param minecraft Minecraft.getMinecraft() instance
     * @param mouseX mouse x
     * @param mouseY mouse y
     * @param partialTicks ticks
     */
    public abstract void draw(@Nonnull Minecraft minecraft, int mouseX, int mouseY, float partialTicks);

    public boolean isHover() {
        return true;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
