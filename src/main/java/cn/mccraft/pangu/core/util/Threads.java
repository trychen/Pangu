package cn.mccraft.pangu.core.util;

import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.server.FMLServerHandler;

public interface Threads {
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
        return Sides.isServer() ? server() : client();
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
}
