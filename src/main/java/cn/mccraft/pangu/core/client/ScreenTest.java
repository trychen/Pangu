package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.asm.dev.DevOnly;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.tooltip.ToolTip;
import cn.mccraft.pangu.core.client.ui.Screen;
import cn.mccraft.pangu.core.client.ui.TextButton;
import cn.mccraft.pangu.core.client.ui.builtin.ColorBackground;
import cn.mccraft.pangu.core.client.ui.builtin.EntityShow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@DevOnly
@SideOnly(Side.CLIENT)
public class ScreenTest extends Screen {
    public ScreenTest() {
        drawDefaultBackground = false;
    }

    @BindKeyPress(description = "key.test", keyCode = Keyboard.KEY_Y, modifier = KeyModifier.CONTROL)
    public static void onKeyDown() {
        Screen screen = new Screen();

        screen
                .addComponent(new EntityShow(Minecraft.getMinecraft().player).setScale(100).setPosition(200, 300))
                .addComponent(new ColorBackground())
                .addComponent(new TextButton("Hello"));

        new ToolTip("Hello World").display(Minecraft.getMinecraft().player);

        Minecraft.getMinecraft().displayGuiScreen(screen);

        System.out.println(Minecraft.getMinecraft().player.getEntityData());
    }
}
