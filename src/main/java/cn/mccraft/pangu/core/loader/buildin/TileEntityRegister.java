package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.annotation.RegTileEntity;
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
                        Class<? extends TileEntity> tileEntity = (Class<? extends TileEntity>) Class.forName(it.getClassName());
                        GameRegistry.registerTileEntity(tileEntity, (String) it.getAnnotationInfo().get("value"));
                        size[0]++;
                    } catch (ClassNotFoundException | ClassCastException e) {
                        e.printStackTrace();
                    }
                });
        PanguCore.getLogger().info("Processed " + size[0] + " TileEntity annotations");
    }
}