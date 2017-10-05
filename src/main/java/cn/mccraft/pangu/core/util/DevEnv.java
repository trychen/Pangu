package cn.mccraft.pangu.core.util;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public enum DevEnv {
    INSTANCE;

    private boolean activing;

    public void setup(){
        final String checkingActivedClassName = "net.minecraft.world.World";
        activing = checkingActivedClassName.equals(FMLDeobfuscatingRemapper.INSTANCE.map(checkingActivedClassName));
    }

    public boolean isActiving() {
        return activing;
    }
}
