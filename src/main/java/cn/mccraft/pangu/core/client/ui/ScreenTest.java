package cn.mccraft.pangu.core.client.ui;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.util.render.Blur;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

//@DevOnly
@SideOnly(Side.CLIENT)
@Blur.Gui
public class ScreenTest extends Screen {
    public ScreenTest() {
        drawDefaultBackground = false;
    }

    @Override
    public void initGui() {
        rootContainer.addComponent(new TextButton("Hello", TextButton.NORMAL));
    }

    @BindKeyPress(description = "key.test", keyCode = Keyboard.KEY_Y, modifier = KeyModifier.CONTROL)
    public static void onKeyDown() {
        Minecraft.getMinecraft().displayGuiScreen(new ScreenTest());
    }
}
