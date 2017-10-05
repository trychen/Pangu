package cn.mccraft.pangu.core.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;

public class LoaderTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("transformedName.equals(net.minecraftforge.fml.common.Loader")) {

        }
        return basicClass;
    }
}
