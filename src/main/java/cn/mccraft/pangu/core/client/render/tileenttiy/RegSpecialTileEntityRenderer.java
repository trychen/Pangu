package cn.mccraft.pangu.core.client.render.tileenttiy;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RegisteringHandler(TileEntityRendererRegister.class)
@SideOnly(Side.CLIENT)
public @interface RegSpecialTileEntityRenderer {
    Class<? extends TileEntity> value();
}
