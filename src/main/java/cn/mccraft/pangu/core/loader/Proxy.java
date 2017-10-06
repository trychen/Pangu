package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 动态代理.
 * 请在主类中监听你要进行注册的{@link FMLStateEvent} 并调用{@link #invoke(FMLStateEvent, LoaderState, Side)}函数.
 *
 * @author LasmGratel
 * @since .2
 */
public enum Proxy {
    INSTANCE;

    /**
     * Set to check if has loaded, to prevent repeat add
     */
    private Set<Object> loadedLoader = new HashSet();

    /**
     * The implementation of {@link Proxy#getStateLoaderMap()}
     */
    private final Map<LoaderState, List<Method>> stateLoaderMap = new HashMap<>();


    /**
     * start invoking the opposite registered loader's
     *
     * @param event the instance of event
     * @param state not all loader state will be invoke in the loader, just for {@link LoaderState#PREINITIALIZATION} to {@link LoaderState#AVAILABLE}
     * @param side  {@link Side}
     * @param <T>   The event invoked by {@link net.minecraftforge.fml.common.Mod.EventHandler}
     * @since .2
     */
    public <T extends FMLStateEvent> void invoke(T event, LoaderState state, Side side) {
        getStateLoaderMap().values().forEach(methods -> methods.forEach(method -> {
            // check side
            if (!method.getAnnotation(Load.class).side().equals(side)) return;
            // check state
            if (!method.getAnnotation(Load.class).value().equals(state)) return;
            // check method parameters
            if (method.getParameterCount() > 1 || (method.getParameterCount() == 1 && !method.getParameterTypes()[0].equals(event.getClass())))
                return;

            // get instance to invoke method
            Object instance = InstanceHolder.getInstance(method.getDeclaringClass());

            // trying to invoke method
            try {
                if (instance == null && !Modifier.isStatic(method.getModifiers())) {
                    throw new NullPointerException("Couldn't find the instance to invoke or method is not static: " + method.toString());
                }

                if (!method.isAccessible()) method.setAccessible(true);

                if (method.getParameterCount() == 0) method.invoke(instance);
                else method.invoke(instance, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                PanguCore.getLogger().warn("Un-able to invoke method " + method.getName(), e);
            }
        }));
    }

    /**
     * RegisteringHandler loader. You should noticed that if your loader is client only,
     * you should register you loader in a {@code @SideOnly(Side.CLIENT)} class.
     * The registering class should have a no-parameter and visible constructor.
     * And the method that annotated by {@link Load} should be also visible, or
     * it won't be register. And specifically, if your class contain an instance
     * field or method, this will search for that.
     *
     * @param object registering object.
     * @return the instance you given or we create
     */
    public Object addLoader(@Nonnull Object object) {
        try {
            // check if object is a class to invoke static load
            boolean isStatic = object instanceof Class;

            // get the class with diff status
            Class<?> loaderClass = isStatic ? (Class<?>) object : object.getClass();

            Object instance = InstanceHolder.getInstance(object);

            // check if loaded
            if (loadedLoader.contains(isStatic ? loaderClass : instance)) return instance;

            // searching method
            for (Method method : loaderClass.getDeclaredMethods())
                // searching annotations
                for (Annotation annotation : method.getDeclaredAnnotations())
                    // checking annotation type
                    if (annotation.annotationType().equals(Load.class))
                        // checking parameter count
                        if (method.getParameterCount() <= 1) {
                            List<Method> methods = getStateLoaderMap().getOrDefault(((Load) annotation).value(), new ArrayList<>());
                            if (!methods.contains(method))
                                methods.add(method);
                            getStateLoaderMap().put(((Load) annotation).value(), methods);
                        }

            loadedLoader.add(isStatic ? loaderClass : instance);
            return instance;
        } catch (Exception e) {
            PanguCore.getLogger().error("Un-able to register loader " + object + ":" + e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * Map that use to mapping the loader state to opposite loader's method
     *
     * @return Map loader state to opposite loader's method
     */
    public Map<LoaderState, List<Method>> getStateLoaderMap() {
        return stateLoaderMap;
    }

    @AnnotationInjector.StaticInvoke
    public static void injectAnnotation(ASMDataTable table) {
        table.getAll(Load.class.getName())
                .stream()
                .map(ASMDataTable.ASMData::getClassName)
                .distinct()
                .forEach(it -> {
                    try {
                        INSTANCE.addLoader(Class.forName(it));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }
}
