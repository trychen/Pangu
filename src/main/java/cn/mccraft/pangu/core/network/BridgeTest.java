package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.client.input.BindKeyPress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public interface BridgeTest {
    @SideOnly(Side.CLIENT)
    @BindKeyPress(Keyboard.KEY_P)
    static void test() {
        remote(null, "Hello");
    }

    @Bridge("Message")
    static void remote(EntityPlayer player, String message) {
        System.out.println(message + " from " + player.getName());
    }
}
