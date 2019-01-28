package cn.mccraft.pangu.core.util;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

public interface Sides {
    boolean isDevEnv = isDeobfuscatedEnvironment();

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
     * Is client environment.
     */
    static boolean isClient() {
        return commonSide().isClient();
    }
    /**
     * Is server environment.
     */
    static boolean isServer() {
        return commonSide().isServer();
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

    static void devOnly(Runnable runnable) {
        if (isDevEnv) runnable.run();
    }
}
