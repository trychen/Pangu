package cn.mccraft.pangu.core.util;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.thread.SidedThreadGroup;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Sides {
    boolean isDevEnv = isDeobfuscatedEnvironment();

    /**
     * Get current thread side
     */
    @Nonnull
    static Side safeCurrentThreadSide() {
        Side side = currentThreadSide();
        return side == null ? Side.CLIENT : side;
    }
    /**
     * Get current thread side.
     * null if is not in minecraft threads.
     */
    @Nullable
    static Side currentThreadSide() {
        final ThreadGroup group = Thread.currentThread().getThreadGroup();
        return group instanceof SidedThreadGroup ? ((SidedThreadGroup) group).getSide() : null;
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
