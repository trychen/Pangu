package cn.mccraft.pangu.core.client.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
@Accessors(chain = true)
public abstract class Screen extends GuiScreen {
    @Setter
    @Getter
    protected GuiScreen parentScreen;

    @Getter
    @Setter
    protected boolean drawDefaultBackground = true;

    @Getter
    @Setter
    protected boolean doesGuiPauseGame = false;

    @Delegate(excludes = Component.class)
    protected Container rootContainer;

    @Getter
    protected Modal modal;

    @Getter
    protected float halfWidth, halfHeight;

    public Screen() {
    }

    @Override
    public void initGui() {
        halfWidth = width / 2F;
        halfHeight = width / 2F;
        rootContainer = new Container(width, height);
        rootContainer.setScreen(this);
        if (getModal() != null) {
            getModal().clear();
            getModal().setSize(width, height);
            getModal().init();
        }
        init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (mc == null) return; // 避免提前打开
        if (drawDefaultBackground) drawDefaultBackground();
        draw();
        rootContainer.onDraw(partialTicks, mouseX, mouseY);
        if (getModal() != null) getModal().onDraw(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        (getModal() == null ? rootContainer : getModal()).onMousePressed(mouseButton, mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return doesGuiPauseGame;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        (getModal() == null ? rootContainer : getModal()).onMouseReleased(mouseX, mouseY);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            if (getModal() != null) {
                getModal().close();
            } else {
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null) this.mc.setIngameFocus();
            }
        } else {
            (getModal() == null ? rootContainer : getModal()).onKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();

        (getModal() == null ? rootContainer : getModal()).onMouseInput(mouseX, mouseY);
    }

    public void setModal(Modal modal) {
        this.modal = modal;
        if (this.modal != null) this.modal.init();
    }

    public void open() {
        if (Minecraft.getMinecraft() != null) setParentScreen(Minecraft.getMinecraft().currentScreen);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    public void closeScreen() {
        if (getModal() != null)
            setModal(null);
        else Minecraft.getMinecraft().displayGuiScreen(parentScreen);
    }

    /**
     * Add your components in this method
     */
    public abstract void init();

    /**
     * Draw background or else
     */
    public void draw() {
    }
}