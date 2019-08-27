package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.toast.ToastData;
import cn.mccraft.pangu.core.client.toast.Toasts;
import cn.mccraft.pangu.core.network.Bridge;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public interface Test {
    @BindKeyPress(Keyboard.KEY_L)
    static void key() {
        test(null, "World");
    }

    @Bridge
    static void test(EntityPlayer player, String hello) {
        Toasts.update(player, new ToastData("Hello"), true);
    }
}
