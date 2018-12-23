package cn.mccraft.pangu.core.client.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class Screen extends GuiScreen {
    protected boolean drawDefaultBackground = true;
    protected Container rootContainer;

    public Screen() {
        rootContainer = new Container(width, height);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (drawDefaultBackground) drawDefaultBackground();
        rootContainer.onDraw(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        rootContainer.onMousePressed(mouseButton, mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        rootContainer.onMouseReleased(mouseX, mouseY);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1){
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) this.mc.setIngameFocus();
        } else {
            rootContainer.onKeyTyped(typedChar, keyCode);
        }
    }
}