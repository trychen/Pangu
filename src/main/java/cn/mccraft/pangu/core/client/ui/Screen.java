package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.client.ui.stack.Spacer;
import cn.mccraft.pangu.core.util.Sides;
import cn.mccraft.pangu.core.util.debug.Message;
import cn.mccraft.pangu.core.util.font.DefaultFontProvider;
import cn.mccraft.pangu.core.util.render.Rect;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
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
    protected boolean drawDefaultBackground = true, drawHUD = true;

    @Getter
    @Setter
    protected boolean doesGuiPauseGame = false;

    @Delegate(excludes = Component.class)
    protected Container rootContainer;

    @Getter
    protected Modal modal;

    @Getter
    protected float halfWidth, halfHeight;

    @Getter
    @Setter
    protected boolean debug;

    /**
     * A screen with center origin means that
     * origin (0, 0) will be treated as screen center (halfWidth, halfHeight)
     */
    @Getter
    @Setter
    protected boolean centerOrigin = false;

    /**
     * False that the {@link Screen#init()} method will be called while resizing screen
     */
    @Getter
    @Setter
    protected boolean noRefreshWithResize = false;

    @Getter
    @Setter
    protected Component tooltips2Render;

    @Getter
    protected long openTime;

    @Getter
    @Setter
    protected int openInputDelay;

    @Getter
    protected boolean canInput = true, ignoreKeyTypeDelay = false;

    @Getter
    @Setter
    protected Component focusedComponent;

    public Screen() {
    }

    @Override
    public void initGui() {
        try {
            if (openInputDelay > 0) {
                canInput = false;
                openTime = Minecraft.getSystemTime();
            }
            halfWidth = width / 2F;
            halfHeight = height / 2F;

            if (noRefreshWithResize) {
                if (rootContainer == null) {
                    rootContainer = new Container(width, height);
                    rootContainer.setScreen(this);
                    init();
                }
                return;
            } else {
                rootContainer = new Container(width, height);
                rootContainer.setScreen(this);
                if (getModal() != null) {
                    getModal().clear();
                    getModal().setSize(width, height);
                    getModal().init();
                }
                init();
            }
        } catch (Throwable e) {
            PanguCore.getLogger().error("Error while init ui " + getClass().toGenericString(), e);
            Message.chat(String.format("UI Error [%s]", e.getLocalizedMessage()));
            closeScreen();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            if (mc == null) return; // 避免提前打开
            if (rootContainer == null) return; // 避免提前打开

            // 触发事件
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent(this));

            // 画默认背景
            if (drawDefaultBackground) drawDefaultBackground();

            boolean hasModal = getModal() != null;

            int contentMouseX = hasModal ? Integer.MAX_VALUE : mouseX;
            int contentMouseY = hasModal ? Integer.MAX_VALUE : mouseY;

            // 画背景
            drawBackground();
            drawBackground(partialTicks, contentMouseX, contentMouseY);

            if (!canInput && openInputDelay > 0 && openTime != 0) {
                if (Minecraft.getSystemTime() - openTime > openInputDelay) {
                    canInput = true;
                }
            }
            draw();
            draw(partialTicks, contentMouseX, contentMouseY);
            rootContainer.onDraw(partialTicks, contentMouseX, contentMouseY);

            // 画调试的中线框
            if (isDebug()) {
                Rect.draw(halfWidth, 0, halfWidth + 1, height, 0xAA00FF00);
                Rect.draw(0, halfHeight, width, halfHeight + 1, 0xAA00FF00);
            }

            drawForeground();
            drawForeground(partialTicks, contentMouseX, contentMouseY);

            if (hasModal) {
                setTooltips2Render(null);
                getModal().onDraw(partialTicks, mouseX, mouseY);
            }

            if (getTooltips2Render() != null) {
                List<String> toolTips = getTooltips2Render().getToolTips();
                if (toolTips != null) getTooltips2Render().drawToolTips(toolTips, mouseX, mouseY);
                setTooltips2Render(null);
            }

            // TODO: It just work
            Rect.drawFullTexTextured(0, 0, 0, 0);
            Rect.resetFiltering();
        } catch (Throwable e) {
            PanguCore.getLogger().error("Error while draw ui " + getClass().toGenericString(), e);
            Message.chat(String.format("UI Error [%s]", e.getLocalizedMessage()));
            closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        try {
            if (!canInput) return;
            (getModal() == null ? rootContainer : getModal()).onMousePressed(mouseButton, mouseX, mouseY);
        } catch (Throwable e) {
            PanguCore.getLogger().error("Error while click ui " + getClass().toGenericString(), e);
            Message.chat(String.format("UI Error [%s]", e.getLocalizedMessage()));
            closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return doesGuiPauseGame;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        try {
            if (!canInput) return;
            (getModal() == null ? rootContainer : getModal()).onMouseReleased(state, mouseX, mouseY);
        } catch (Throwable e) {
            PanguCore.getLogger().error("Error while release ui " + getClass().toGenericString(), e);
            Message.chat(String.format("UI Error [%s]", e.getLocalizedMessage()));
            closeScreen();
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        try {
            if (!canInput && !ignoreKeyTypeDelay) return;
            if (Sides.isDeobfuscatedEnvironment()) debugShortcut(keyCode);
            if (keyCode == 1) {
                closeScreen();
            } else {
                (getModal() == null ? rootContainer : getModal()).onKeyTyped(typedChar, keyCode);
                typed(typedChar, keyCode);
            }
        } catch (Throwable e) {
            PanguCore.getLogger().error("Error while type ui " + getClass().toGenericString(), e);
            Message.chat(String.format("UI Error [%s]", e.getLocalizedMessage()));
            closeScreen();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        if (!canInput) return;
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();

        (getModal() == null ? rootContainer : getModal()).onMouseInput(mouseX, mouseY);
    }

    public void setModal(Modal modal) {
        if (openInputDelay > 0) {
            canInput = false;
            openTime = Minecraft.getSystemTime();
        }
        this.modal = modal;
        if (this.modal != null) {
            this.modal.setScreen(this);
            this.modal.init();
        }
    }

    public void open() {
        open(Minecraft.getMinecraft().currentScreen);
    }

    public void open(GuiScreen parent) {
        setParentScreen(parent);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    public void closeScreen() {
        if (getModal() != null)
            setModal(null);
        else {
            Minecraft.getMinecraft().displayGuiScreen(parentScreen);
            if (parentScreen == null) this.mc.setIngameFocus();
        }
    }

    @Override
    public void onGuiClosed() {
        onClose();
        rootContainer.onScreenClose();
    }

    public void debugShortcut(int key) {
        if (key == Keyboard.KEY_D && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            setDebug(!isDebug());
        }
    }

    public void drawHovering(Component component, List<String> texts, int mouseX, int mouseY) {
        GuiUtils.drawHoveringText(texts, mouseX, mouseY, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, -1, Minecraft.getMinecraft().fontRenderer);
        RenderHelper.disableStandardItemLighting();
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

    /**
     * On Screen Close
     */
    public void onClose() {
    }

    /**
     * Draw background or else
     */
    public void draw(float partialTicks, int mouseX, int mouseY) {
    }

    /**
     * On key typed
     */
    public void typed(char typedChar, int keyCode) {
    }


    public Stack HStack(Component... components) {
        return UI.horizontal(this, components);
    }

    public Stack VStack(Component... components) {
        return UI.vertical(this, components);
    }

    public Spacer Spacer(float width, float height) {
        return Spacer.of(width, height);
    }

    public Spacer Spacer(float size) {
        return Spacer.of(size, size);
    }

    public Spacer HSpacer(float size) {
        return Spacer.of(size, 0);
    }

    public Spacer VSpacer(float size) {
        return Spacer.of(0, size);
    }

}