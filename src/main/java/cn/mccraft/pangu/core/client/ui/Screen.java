package cn.mccraft.pangu.core.client.ui;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
@Accessors(chain = true)
public class Screen extends GuiScreen {
    @Getter
    @Setter
    protected boolean drawDefaultBackground = true;

    @Getter
    @Setter
    protected boolean doesGuiPauseGame = false;

    @Delegate(excludes = Component.class)
    protected Container rootContainer;

    public Screen() {
        rootContainer = new Container(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
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
    public boolean doesGuiPauseGame() {
        return doesGuiPauseGame;
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