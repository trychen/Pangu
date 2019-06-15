package cn.mccraft.pangu.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Games {
    /**
     * Is running with integrated server
     */
    static boolean isIntegratedServer() {
        return Sides.isClient() && Minecraft.getMinecraft().isIntegratedServerRunning();
    }

    static EntityPlayer player() {
        return Minecraft.getMinecraft().player;
    }

    static Minecraft minecraft() {
        return Minecraft.getMinecraft();
    }
}
