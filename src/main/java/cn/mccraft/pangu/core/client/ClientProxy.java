package cn.mccraft.pangu.core.client;

import cn.mccraft.pangu.core.CommonProxy;
import cn.mccraft.pangu.core.loader.Proxy;
import cn.mccraft.pangu.core.util.resource.PanguResLoc;
import net.minecraft.util.ResourceLocation;
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

    static {
        Proxy.INSTANCE.addLoader(ModelLoader.class);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        Proxy.INSTANCE.invoke(event, LoaderState.PREINITIALIZATION, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Proxy.INSTANCE.invoke(event, LoaderState.INITIALIZATION,Side.CLIENT);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        Proxy.INSTANCE.invoke(event, LoaderState.POSTINITIALIZATION,Side.CLIENT);
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
        super.loadComplete(event);
        Proxy.INSTANCE.invoke(event, LoaderState.AVAILABLE,Side.CLIENT);
    }
}
