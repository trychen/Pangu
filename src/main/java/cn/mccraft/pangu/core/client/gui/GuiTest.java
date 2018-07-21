package cn.mccraft.pangu.core.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static cn.mccraft.pangu.core.client.ClientProxy.PG_ICONS_TEXTURE;

@SideOnly(Side.CLIENT)
public class GuiTest extends GuiScreen {
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        addButton(new TextButton(1, 0, 0, "Hello Text"));
        addButton(new TextButton(2, 0, 50, 100, 20, "Hello Button", TextButton.Style.PRIMARY).setTextCenterAlign(false));
        addButton(new TextButton(3, 0, 100, "Hello DARK", TextButton.Style.DARK));
        addButton(new TextButton(4, 0, 150, "Hello WHITE", TextButton.Style.WHITE));
        addButton(new IconButton(5, 100, 0, PG_ICONS_TEXTURE, 11));
        addButton(new IconButton(6, 150, 0, PG_ICONS_TEXTURE, 11, 0, 11, IconButton.Style.CONTAINER));
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
