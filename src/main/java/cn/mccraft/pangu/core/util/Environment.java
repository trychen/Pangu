package cn.mccraft.pangu.core.util;

import net.minecraftforge.fml.relauncher.Side;

/**
 * 环境检查
 *
 * @since 1.0.0.2
 * @deprecated {@link Sides}
 */
@Deprecated
public interface Environment {
    boolean isDevEnv = isDeobfuscatedEnvironment();

    static boolean isDeobfuscatedEnvironment() {
        return Sides.isDeobfuscatedEnvironment();
    }

    static void devOnly(Runnable runnable) {
        Sides.devOnly(runnable);
    }

    static Side side() {
        return Sides.commonSide();
    }
}
