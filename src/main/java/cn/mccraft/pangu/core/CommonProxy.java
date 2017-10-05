package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.capability.CapabilityLoader;
import cn.mccraft.pangu.core.item.PanguItems;
import cn.mccraft.pangu.core.loader.Proxy;
import cn.mccraft.pangu.core.loader.Register;
import cn.mccraft.pangu.core.loader.buildin.ItemRegister;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Common proxy of Pangu Core
 * Pangu Core 公共代理
 *
 * @author trychen
 * @since .2
 */
public class CommonProxy {

    public CommonProxy() {
        Register.INSTANCE.register(PanguItems.INSTANCE);
    }

    /*
     *  ==============================
     *        FML Initialization
     */
    public void preInit(FMLPreInitializationEvent event) {
        Proxy.INSTANCE.invoke(event, LoaderState.PREINITIALIZATION, Side.SERVER);
        // TODO
        new CapabilityLoader().registerCapabilities();
    }

    public void init(FMLInitializationEvent event) {
        Proxy.INSTANCE.invoke(event, LoaderState.INITIALIZATION, Side.SERVER);
    }

    public void postInit(FMLPostInitializationEvent event) {
        Proxy.INSTANCE.invoke(event, LoaderState.POSTINITIALIZATION, Side.SERVER);
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        Proxy.INSTANCE.invoke(event, LoaderState.AVAILABLE, Side.SERVER);
    }
    /*
     *        FML Initialization
     *  ==============================
     */

}
