package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 动态代理.
 * 请在主类中监听你要进行注册的{@link FMLStateEvent} 并调用{@link #invoke(FMLStateEvent, LoaderState, Side)}函数.
 *
 * @author LasmGratel
 * @since .2
 */
public interface Proxy {
    /**
     * start invoking the opposite registered loader's
     *
     * @param event the instance of event
     * @param state not all loader state will be invoke in the loader, just for {@link LoaderState#PREINITIALIZATION} to {@link LoaderState#AVAILABLE}
     * @param side {@link Side}
     * @param <T> The event invoked by {@link net.minecraftforge.fml.common.Mod.EventHandler}
     */
    default <T extends FMLStateEvent> void invoke (T event, LoaderState state, Side side) {
        getStateLoaderMap().values().forEach(methods -> methods.forEach(method -> {
            // 判断 Side 是否对应
            if (method.getAnnotation(Load.class).side().equals(side))
                if (method.getParameterCount() == 0 && method.getAnnotation(Load.class).value().equals(state))
                    try {
                        method.invoke(getLoaderInstanceMap().get(method.getDeclaringClass()));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        PanguCore.getLogger().warn("Un-able to invoke method " + method.getName(), e);
                    }
                else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(event.getClass()))
                    try {
                        method.invoke(getLoaderInstanceMap().get(method.getDeclaringClass()), event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        PanguCore.getLogger().warn("Un-able to invoke method " + method.getName(), e);
                    }
        }));
    }

    default <T> Optional<T> getLoader(Class<T> loaderClass) {
        try {
            return Optional.of((T) getLoaderInstanceMap().get(loaderClass));
        } catch (ClassCastException ignored) {
            return Optional.empty();
        }
    }

    /**
     * Registering loader. You should noticed that if your loader is client only,
     * you should register you loader in a {@code @SideOnly(Side.CLIENT)} class.
     * The registering class should have a no-parameter and visible constructor.
     * And the method that annotated by {@link Load} should be also visible, or
     * it won't be register.
     *
     * @param loaderClass
     */
    default void addLoader(Class<?> loaderClass) {
        try {
            //加载所有方法
            for (Method method : loaderClass.getMethods())
                for (Annotation annotation : method.getDeclaredAnnotations())
                    if (annotation.annotationType().equals(Load.class))
                        if (method.getParameterCount() <= 1) {
                            List<Method> methods = getStateLoaderMap().getOrDefault(((Load) annotation).value(), new ArrayList<>());
                            if (!methods.contains(method))
                                methods.add(method);
                            getStateLoaderMap().put(((Load) annotation).value(), methods);
                        }
            getLoaderInstanceMap().put(loaderClass, loaderClass.newInstance());
        } catch (Exception e) {
            PanguCore.getLogger().error("Un-able to register loader " + loaderClass.getName() + ":" + e.getLocalizedMessage());
        }
    }

    /**
     * Map that use to mapping the loader state to opposite loader's method,
     * Using List instead of collection is to fake priority level.
     *
     * @return Map loader state to opposite loader's method
     */
    Map<LoaderState, List<Method>> getStateLoaderMap();

    /**
     * Map that use to mapping loader's class to instance
     *
     * @return Map Class2Object
     */
    Map<Class<?>, Object> getLoaderInstanceMap();

}
