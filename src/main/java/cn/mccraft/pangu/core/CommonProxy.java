package cn.mccraft.pangu.core;

import cn.mccraft.pangu.core.loader.Proxy;
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
public class CommonProxy implements Proxy {

    public CommonProxy() {
        addLoader(LoggerLoader.class);
    }

    /**
     * The default loaders by Pangu Core
     */
    private final Collection<Class<?>> loaders = Arrays.asList();

    /**
     * The implementation of {@link CommonProxy#getLoaderInstanceMap()}
     */
    private final Map<Class<?>, Object> loaderInstanceMap = new HashMap<>();

    /**
     * The implementation of {@link CommonProxy#getStateLoaderMap()}
     */
    private final Map<LoaderState, List<Method>> stateLoaderMap = new HashMap<>();

    public void preInit(FMLPreInitializationEvent event) {
        invoke(event, LoaderState.PREINITIALIZATION, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {
        invoke(event, LoaderState.INITIALIZATION, Side.SERVER);
    }

    public void postInit(FMLPostInitializationEvent event) {
        invoke(event, LoaderState.POSTINITIALIZATION, Side.SERVER);
    }

    public void loadComplete(FMLLoadCompleteEvent event) {
        invoke(event, LoaderState.AVAILABLE, Side.SERVER);
    }

    /**
     * Map that use to mapping the loader state to opposite loader's method
     *
     * @return Map loader state to opposite loader's method
     */
    @Override
    public Map<LoaderState, List<Method>> getStateLoaderMap() {
        return stateLoaderMap;
    }

    /**
     * Map that use to mapping loader's class to instance
     *
     * @return Map Class2Object
     */
    @Override
    public Map<Class<?>, Object> getLoaderInstanceMap() {
        return loaderInstanceMap;
    }
}
