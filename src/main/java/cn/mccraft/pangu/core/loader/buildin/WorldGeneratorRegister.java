package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.annotation.RegWorldGenerator;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

public class WorldGeneratorRegister {
    @AnnotationInjector.StaticInvoke
    public static void registerWorldGenerator(ASMDataTable table) {
        table.getAll(RegWorldGenerator.class.getName())
                .forEach(it -> {
                    try {
                        Class clazz = Class.forName(it.getClassName());
                        if (it.getClassName().equals(it.getObjectName())) {
                            // TODO:
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }
}
