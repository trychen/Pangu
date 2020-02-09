package cn.mccraft.pangu.core.client.render.tileenttiy;

import cn.mccraft.pangu.core.loader.AnnotationRegister;
import cn.mccraft.pangu.core.loader.AutoWired;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@AutoWired
public class TileEntityRendererRegister implements AnnotationRegister<RegSpecialTileEntityRenderer, TileEntitySpecialRenderer> {
    @Override
    public void registerClass(Class<? extends TileEntitySpecialRenderer> clazz, RegSpecialTileEntityRenderer meta, String domain) {
        ClientRegistry.bindTileEntitySpecialRenderer(meta.value(), ReflectUtils.forInstance(clazz));
    }
}
