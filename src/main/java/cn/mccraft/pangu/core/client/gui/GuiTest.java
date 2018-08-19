package cn.mccraft.pangu.core.client.gui;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import static cn.mccraft.pangu.core.client.PGClient.PG_ICONS_TEXTURE;

@DevOnly
@SideOnly(Side.CLIENT)
public class GuiTest extends GuiScreen {
    @Override
    public void initGui() {
        addButton(new TextButton(1, 0, 0, "Hello Text"));
        addButton(new TextButton(2, 0, 50, 100, 20, "Hello Button", TextButton.PRIMARY).setTextCenterAlign(false));
        addButton(new TextButton(3, 0, 100, "Hello DARK", TextButton.DARK));
        addButton(new TextButton(4, 0, 150, "Hello WHITE", TextButton.WHITE));
        addButton(new IconButton(5, 100, 0, PG_ICONS_TEXTURE, 11).setEnabled(false));
        addButton(new IconButton(6, 150, 0, PG_ICONS_TEXTURE, 11, 0, 11, IconButton.CONTAINER));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        System.out.println(button.id);
    }
}
