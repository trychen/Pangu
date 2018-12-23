package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.tooltip.ToolTip;
import cn.mccraft.pangu.core.client.tooltip.ToolTipRenderer;
import cn.mccraft.pangu.core.network.RemoteTester;
import cn.mccraft.pangu.core.util.render.Blur;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@DevOnly
@SideOnly(Side.CLIENT)
@Blur.Gui(radius = 8)
public class GuiTest extends GuiScreen {
    @Override
    public void initGui() {
//        addButton(new TextLabel(99, 5, 5, 50, "返回游戏", SANS_SERIF).setRenderBox(true));
//        addButton(new TextButton(1, 0, 0, "Hello Text"));
//        addButton(new TextButton(2, 0, 50, 100, 20, "Hello Button", TextButton.PRIMARY).setTextCenterAlign(false));
//        addButton(new TextButton(3, 0, 100, "Hello DARK", TextButton.DARK));
        addButton(new TextButton(4, 0, 150, "Hello WHITE", TextButton.WHITE));
//        addButton(new IconButton(5, 100, 0, Icons.DOT_GREEN).setEnabled(false));
        addButton(new IconButton(6, 0, 100, IconButton.GREEN, Icons.DOT_GREEN));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        Rect.drawBox(100, 100, 30, 30, 0xFFFF0000);

        Rect.drawGradient(0, 0, width, 30, 0xFFFFFFFF, 0xFFFFFFFF, 0x00000000, 0x00000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        System.out.println(button.id);
    }

    @BindKeyPress(description = "key.test", keyCode = Keyboard.KEY_Y)
    public static void onKeyDown() {
        try {
            RemoteTester.test(null, "你好");
        } catch (Exception e) {
            PanguCore.getLogger().error("", e);
        }
//        ToolTipRenderer.INSTANCE.set(new ToolTip("Chenzhilin 红石 800/1750").setDuration(100).setAnimated(false));
//        Minecraft.getMinecraft().displayGuiScreen(new GuiTest());
    }
}
