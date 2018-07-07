package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.IRegister;
import cn.mccraft.pangu.core.loader.annotation.RegTileEntity;
import cn.mccraft.pangu.core.util.resource.PanguResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;

@AutoWired
public class TileEntityRegister implements IRegister<RegTileEntity, TileEntity> {
    @Override
    public void registerField(Field field, TileEntity tileEntity, RegTileEntity annotation) {
    }

    @Override
    public void registerClass(Class<? extends TileEntity> tileEntity, RegTileEntity regTileEntity) {
        GameRegistry.registerTileEntity(tileEntity, PanguResourceLocation.of(regTileEntity.value()));
    }
}