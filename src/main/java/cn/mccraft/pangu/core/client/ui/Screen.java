package cn.mccraft.pangu.core.client.ui;

import net.minecraft.client.gui.GuiScreen;

public class Screen extends GuiScreen {
    @Override
    public void drawBackground(int tint) {
        this.drawDefaultBackground();
        super.drawBackground(tint);
    }
}
