package cn.mccraft.pangu.core.annotation;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 动态代理.
 * 请在主类中监听你要进行注册的{@link FMLStateEvent} 并调用{@link #invoke(FMLStateEvent, LoaderState, Side)}函数.
 *
 * @author LasmGratel
 * @since .2
 */
public interface Proxy {
    default <T extends FMLStateEvent> void invoke (T event, LoaderState state, Side side) {
        getStateLoaderMap().values().forEach(methods -> methods.forEach(method -> {
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

    Map<Class<?>, Object> getLoaderInstanceMap();

    @SuppressWarnings("unchecked")
    default <T> Optional<T> getLoader(Class<T> loaderClass) {
        try {
            return Optional.of((T) getLoaderInstanceMap().get(loaderClass));
        } catch (ClassCastException ignored) {
            return Optional.empty();
        }
    }

    default void addLoader(Class<?> loaderClass) {
        try {
            for (Method method : loaderClass.getMethods())
                for (Annotation annotation : method.getDeclaredAnnotations())
                    if (annotation.annotationType().equals(Load.class))
                        if (method.getParameterCount() <= 1) {
                            Collection<Method> methods = getStateLoaderMap().getOrDefault(((Load) annotation).value(), new ArrayList<>());
                            if (!methods.contains(method))
                                methods.add(method);
                            getStateLoaderMap().put(((Load) annotation).value(), methods);
                        }
            getLoaderInstanceMap().put(loaderClass, loaderClass.newInstance());
        } catch (Exception e) {
            PanguCore.getLogger().error("Un-able to register loader " + loaderClass.getName() + ":" + e.getLocalizedMessage());
        }
    }

    Map<LoaderState, Collection<Method>> getStateLoaderMap();
}
