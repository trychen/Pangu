package cn.mccraft.pangu.core.util;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

public interface Sides {
    /**
     * Get client side thread listener
     */
    @SideOnly(Side.CLIENT)
    static IThreadListener client() {
        return Minecraft.getMinecraft();
    }

    /**
     * Get server side thread listener
     */
    @SideOnly(Side.SERVER)
    static IThreadListener server() {
        return FMLServerHandler.instance().getServer();
    }

    /**
     * Get thread listener safely
     */
    static IThreadListener safe() {
        return commonSide().isServer() ? server() : client();
    }

    /**
     * Submit to safe side
     */
    static ListenableFuture<Object> submit(Runnable runnable) {
        return safe().addScheduledTask(runnable);
    }

    static void submit(Runnable runnable, boolean onCurrentThread) {
        if (onCurrentThread) {
            runnable.run();
        } else {
            submit(runnable);
        }
    }

    /**
     * Get current thread side
     */
    static Side currentThreadSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    /**
     * Get environment side
     */
    static Side commonSide() {
        return FMLLaunchHandler.side();
    }

    /**
     * Is running with integrated server
     */
    static boolean isIntegratedServer() {
        return commonSide().isClient() && Minecraft.getMinecraft().isIntegratedServerRunning();
    }

    /**
     * Get the opposed size
     */
    static Side oppose(Side side) {
        return side.isServer() ? Side.CLIENT : Side.SERVER;
    }

    static boolean isDeobfuscatedEnvironment() {
        return Launch.blackboard.get("fml.deobfuscatedEnvironment") == Boolean.TRUE;
    }

    boolean isDevEnv = isDeobfuscatedEnvironment();

    static void devOnly(Runnable runnable) {
        if (isDevEnv) runnable.run();
    }
}
