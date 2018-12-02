package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.util.render.Blur;
import cn.mccraft.pangu.core.util.render.CustomFont;
import cn.mccraft.pangu.core.util.render.Rect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@DevOnly
@SideOnly(Side.CLIENT)
@Blur.Gui(fadeTime = 3000)
public class GuiTest extends GuiScreen {
    public CustomFont font = new CustomFont("Phosphate", 50);

    @Override
    public void initGui() {
//        addButton(new TextButton(1, 0, 0, "Hello Text"));
//        addButton(new TextButton(2, 0, 50, 100, 20, "Hello Button", TextButton.PRIMARY).setTextCenterAlign(false));
//        addButton(new TextButton(3, 0, 100, "Hello DARK", TextButton.DARK));
//        addButton(new TextButton(4, 0, 150, "Hello WHITE", TextButton.WHITE));
//        addButton(new IconButton(5, 100, 0, Icons.DOT_GREEN).setEnabled(false));
//        addButton(new IconButton(6, 150, 0, IconButton.CONTAINER, Icons.POINTER_RIGHT));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        font.drawCenteredString("Hello", width / 2, height / 2, 0xFFFFFFFF, false);

        Rect.drawGradient(0, 0, width, 30, 0xFFFFFFFF, 0xFFFFFFFF, 0x00000000, 0x00000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        System.out.println(button.id);
    }

    @BindKeyPress(description = "key.test", keyCode = Keyboard.KEY_Y)
    public static void onKeyDown() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTest());
    }
}
