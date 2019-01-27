package cn.mccraft.pangu.core.loader.buildin;

import cn.mccraft.pangu.core.loader.AnnotationRegister;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.loader.annotation.RegTileEntity;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.tileentity.TileEntity;

@AutoWired
public class TileEntityRegister implements AnnotationRegister<RegTileEntity, TileEntity> {
    @Override
    public void registerClass(Class<? extends TileEntity> tileEntity, RegTileEntity regTileEntity, String domain) {
        TileEntity.register(PanguResLoc.of(domain, regTileEntity.value()).toString(), tileEntity);
    }
}