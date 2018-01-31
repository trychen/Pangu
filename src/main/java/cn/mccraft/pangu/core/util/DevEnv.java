package cn.mccraft.pangu.core.util;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

/**
 * 环境检查
 *
 * @since 1.0.0.2
 */
public enum DevEnv {
    INSTANCE;

    private boolean isDevEnv;

    public void setup(){
        final String checkingActivedClassName = "net.minecraft.world.World";
        isDevEnv = checkingActivedClassName.equals(FMLDeobfuscatingRemapper.INSTANCE.map(checkingActivedClassName));
    }

    public boolean isDevEnv() {
        return isDevEnv;
    }
}
