package cn.mccraft.pangu.core.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface Games {
    Minecraft minecraft = Minecraft.getMinecraft();
    /**
     * Is running with integrated server
     */
    static boolean isIntegratedServer() {
        return minecraft.isIntegratedServerRunning();
    }

    static EntityPlayer player() {
        return minecraft.player;
    }

    static Minecraft minecraft() {
        return minecraft;
    }

    static GuiScreen currentScreen() {
        return minecraft.currentScreen;
    }

    static <T extends GuiScreen> T currentScreen(Class<T> aim) {
        if (currentScreen() == null) return null;
        if (aim.isAssignableFrom(currentScreen().getClass()))
            return (T) minecraft.currentScreen;
        else return null;
    }

    static void startProfile(String profile) {
        minecraft.profiler.startSection(profile);
    }

    static void endProfile() {
        minecraft.profiler.endSection();
    }
}
