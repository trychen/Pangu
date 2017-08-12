package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.CommonProxy;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Client proxy of Pangu Core
 * Pangu Core 客户端代理
 *
 * @author trychen
 * @since .2
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        invoke(event, LoaderState.PREINITIALIZATION, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        invoke(event, LoaderState.INITIALIZATION,Side.CLIENT);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        invoke(event, LoaderState.POSTINITIALIZATION,Side.CLIENT);
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
        super.loadComplete(event);
        invoke(event, LoaderState.AVAILABLE,Side.CLIENT);
    }
}
