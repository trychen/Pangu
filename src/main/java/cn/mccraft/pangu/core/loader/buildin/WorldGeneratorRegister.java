package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.annotation.RegTileEntity;
import cn.mccraft.pangu.core.loader.annotation.RegWorldGenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGeneratorRegister {
    @AnnotationInjector.StaticInvoke
    public static void registerWorldGenerator(ASMDataTable table) {
        table.getAll(RegWorldGenerator.class.getName())
                .forEach(it -> {
                    try {
                        Class clazz = Class.forName(it.getClassName());
                        if (it.getClassName().equals(it.getObjectName())) {

                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }
}
