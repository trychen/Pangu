package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 动态代理.
 * 请在主类中监听你要进行注册的{@link FMLStateEvent} 并调用{@link #invoke(FMLStateEvent, LoaderState, Side)}函数.
 *
 * @author LasmGratel
 * @since 1.0.0.2
 */
public enum Proxy {
    INSTANCE;

    private static Map<Class<? extends FMLStateEvent>, LoaderState> eventType2State = new HashMap<>();

    static {
        eventType2State.put(FMLConstructionEvent.class, LoaderState.CONSTRUCTING);
        eventType2State.put(FMLPreInitializationEvent.class, LoaderState.PREINITIALIZATION);
        eventType2State.put(FMLInitializationEvent.class, LoaderState.INITIALIZATION);
        eventType2State.put(FMLPostInitializationEvent.class, LoaderState.POSTINITIALIZATION);
        eventType2State.put(FMLLoadCompleteEvent.class, LoaderState.AVAILABLE);
        eventType2State.put(FMLServerAboutToStartEvent.class, LoaderState.SERVER_ABOUT_TO_START);
        eventType2State.put(FMLServerStartingEvent.class, LoaderState.SERVER_STARTING);
        eventType2State.put(FMLServerStartedEvent.class, LoaderState.SERVER_STARTED);
        eventType2State.put(FMLServerStoppingEvent.class, LoaderState.SERVER_STOPPING);
        eventType2State.put(FMLServerStoppedEvent.class, LoaderState.SERVER_STOPPED);
    }

    /**
     * Set to check if has loaded, to prevent repeat add
     * <p>
     * 用于检查类是否已经被加载过，避免因重复加载而重复执行
     */
    private Set<Object> loadedLoader = new HashSet();

    /**
     * 存储已经加载了的使用了 {@link Load} 注解方法
     */
    private final Map<LoaderState, List<Method>> stateLoaderMap = new HashMap<>();

    /**
     * tart invoking the opposite registered loader's
     *
     * @param event the instance of event
     * @param state not all loader state will be invoke in the loader, just for {@link LoaderState#PREINITIALIZATION} to {@link LoaderState#AVAILABLE}
     * @param side  {@link Side}
     * @param <T>   The event invoked by {@link net.minecraftforge.fml.common.Mod.EventHandler}
     * @since 1.0.0.2
     */
    public <T extends FMLStateEvent> void invoke(T event, LoaderState state, Side side) {
        PanguCore.getLogger().info("Start invoke FML event " + event.getEventType() + " in side " + side.name());
        List<Method> methods = getStateLoaderMap().get(state);

        if (methods == null || methods.isEmpty()) return;

        // bar display
        ProgressManager.ProgressBar bar = ProgressManager.push("Invoking " + state.getPrettyName(), methods.size());

        methods.forEach(method -> {
            bar.step(method.getDeclaringClass().getSimpleName());
            Load load = method.getAnnotation(Load.class);
            // check side
            if (!load.side().equals(side)) return;
            // check state
            // no longer needed for multi action & auto
//            if (!load.value().equals(state)) return;
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
            } catch (Exception e) {
                PanguCore.getLogger().error("Un-able to invoke method " + method.getName(), e);
            }
        });
        ProgressManager.pop(bar);
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
            for (Method method : loaderClass.getDeclaredMethods()) {
                Load load = method.getAnnotation(Load.class);
                if (load == null) continue;
                // checking parameter count
                LoaderState state = load.value();
                if (method.getParameterCount() == 1) {
                    boolean valid = FMLStateEvent.class.isAssignableFrom(method.getParameterTypes()[0]);
                    if (valid) {
                        state = eventType2State.get(method.getParameterTypes()[0]);
                    }

                    if (!valid || state == null) {
                        PanguCore.getLogger().error("Invalid parameter type " + method.getParameterTypes()[0].toGenericString() + " for method " + method.toGenericString());
                        continue;
                    }
                }
                List<Method> methods = getStateLoaderMap().computeIfAbsent(state, it -> new ArrayList<>());
                if (!methods.contains(method)) methods.add(method);
                getStateLoaderMap().put(state, methods);
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
    public static void injectAnnotation(AnnotationStream<Load> anno) {
        anno.fieldAndMethodOwnerClassStream().forEach(INSTANCE::addLoader);
    }
}
