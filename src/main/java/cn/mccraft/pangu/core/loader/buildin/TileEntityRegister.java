package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.annotation.RegTileEntity;
import cn.mccraft.pangu.core.util.resource.PanguResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityRegister {
    @AnnotationInjector.StaticInvoke
    public static void registerTileEntity(ASMDataTable table) {
        final int[] size = {0};
        table.getAll(RegTileEntity.class.getName())
                .stream()
                .filter(it -> it.getClassName().equals(it.getObjectName()))
                .forEach(it -> {
                    try {
                        Class clazz = Class.forName(it.getClassName());
                        if (!TileEntity.class.isAssignableFrom(clazz)) {
                            return;
                        }
                        //noinspection unchecked
                        GameRegistry.registerTileEntity(clazz, PanguResourceLocation.of((String) it.getAnnotationInfo().get("value")));
                        size[0]++;
                    } catch (Exception ex) {
                        PanguCore.getLogger().error("Unable to register " + it.getClassName(), ex);
                    }
                });
        PanguCore.getLogger().info("Processed " + size[0] + " TileEntity annotations");
    }
}