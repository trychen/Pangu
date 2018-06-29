package cn.mccraft.pangu.core.asm;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

public class PanguProxyTweaker implements ITweaker {
    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
//        classLoader.registerTransformer("cn.mccraft.pangu.core.asm.transformer.LoaderTransformer");
        classLoader.registerTransformer("cn.mccraft.pangu.core.asm.dev.DevTransformer");
    }

    @Override
    public String getLaunchTarget() {
        return null;
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

}
