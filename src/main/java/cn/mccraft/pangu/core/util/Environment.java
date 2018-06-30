package cn.mccraft.pangu.core.util;

import net.minecraft.launchwrapper.Launch;

/**
 * 环境检查
 *
 * @since 1.0.0.2
 */
public interface Environment {
    static boolean isDeobfuscatedEnvironment() {
        return Launch.blackboard.get("fml.deobfuscatedEnvironment") == Boolean.TRUE;
    }

    boolean isDevEnv = isDeobfuscatedEnvironment();

    static void devOnly(Runnable runnable) {
        if (isDevEnv) runnable.run();
    }
}
