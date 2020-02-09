package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.util.ASM;
import cn.mccraft.pangu.core.util.ReflectUtils;
import cn.mccraft.pangu.core.util.Sides;
import lombok.val;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ModDiscoverer;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * auto annotation discover
 *
 * @author trychen
 * @since 1.0.2
 */
public enum AnnotationInjector {
    INSTANCE;

    /**
     * cached discoverer
     */
    private static ModDiscoverer discoverer;
    private Set<String> unsafeClass = Collections.EMPTY_SET;

    /**
     * get {@see Loader#discoverer} by {@link ReflectUtils}
     * <p>
     * 通过反射工具类获取 {@see Loader#discoverer}
     */
    public static ModDiscoverer getDiscoverer() {
        if (discoverer == null) {
            discoverer = ReflectUtils.getField(Loader.class, Loader.instance(), "discoverer", ModDiscoverer.class, true);

            if (discoverer == null)
                throw new RuntimeException("Unable to get ModDiscoverer to hook annotation, please check SecurityManager to make sure that reflection is enabled");
            PanguCore.getLogger().debug("Got Loader#discoverer successfully.");
        }
        return discoverer;
    }

    public boolean isSafeClass(ASMDataTable.ASMData check) {
        return !unsafeClass.contains(check.getClassName());
    }

    /**
     * start solve all annotation injector
     * 开始注入 AutoWired
     */
    public void startSolveAutoWireds() {
        PanguCore.getLogger().info("Start solve @AutoWired");

        // 获取 ASMTable
        InstanceHolder.putInstance(getDiscoverer().getASMTable());
        PanguCore.getLogger().debug("Put instance of ASMTable successfully");

        unsafeClass = AnnotationStream.of(SideOnly.class)
                .parallelStream()
                .filter(it -> it.getClassName().equals(it.getObjectName()))
                .filter(it -> {
                    if (it.getClassName().startsWith("net.minecraft")) return false;
                    return !((ModAnnotation.EnumHolder) it.getAnnotationInfo().get("value")).getValue().equals(Sides.commonSide().name());
                })
                .map(ASMDataTable.ASMData::getClassName)
                .collect(Collectors.toSet());

        val stream = AnnotationStream.of(AutoWired.class);

        // solve types
        // 注入初始化类型
        PanguCore.getLogger().debug("Creating instance of @AutoWired type");
        stream
                .typeStream()
                .forEach(it -> {
                    try {
                        final Object instance = InstanceHolder.getOrNewInstance(it);

                        // 注册监听器
                        if (it.getAnnotation(AutoWired.class).registerCommonEventBus()) {
                            MinecraftForge.EVENT_BUS.register(instance);
                        }
                    } catch (Throwable e) {
                        PanguCore.getLogger().error("Unable to inject type " + it.toGenericString(), e);
                    }
                });


        // solve field
        // 解决类型注入
        PanguCore.getLogger().debug("Solving instance of @AutoWired field");
        stream
                .fieldStream()
                .forEach(field -> {
                    // get annotation info
                    val annotation = field.getAnnotation(AutoWired.class);
                    // get typeClass
                    Class typeClass = annotation.value();
                    // set type as typeClass if typeClass is equals to Object.class
                    if (typeClass == Object.class) typeClass = field.getType();
                    // getting instance from InstanceHolder
                    Object object = InstanceHolder.getInstance(typeClass);

                    if (object == null) {
                        PanguCore.getLogger().error("Couldn't found instance of " + typeClass, new NullPointerException());
                    } else {
                        try {
                            field.setAccessible(true);
                            field.set(InstanceHolder.getInstance(field.getDeclaringClass()), object);
                        } catch (Throwable e) {
                            PanguCore.getLogger().error("Couldn't wire field " + field.toGenericString(), e);
                        }
                    }
                });
    }

    public void startSolveInjectors() {
        PanguCore.getLogger().info("Start solve @AnnotationInjector.StaticInvoke");
        AnnotationStream.of(StaticInvoke.class)
                .classStream()
                .forEach(clazz -> {
                    // get instance
                    Object clazzInstance = InstanceHolder.getInstance(clazz);
                    Arrays.stream(clazz.getMethods())
                            // filter that clean non-annotated method
                            .filter(method -> Arrays.stream(method.getAnnotations()).anyMatch(anno -> anno instanceof StaticInvoke))
                            .forEach(method -> startSolveInjectorMethod(method, clazzInstance));
                });
    }

    public void startSolveInjectorMethod(@Nonnull Method method, @Nullable Object instance) {
        if (!Modifier.isStatic(method.getModifiers()) && instance == null) return;

        try {
            Object[] paraObjects = Arrays.stream(method.getGenericParameterTypes()).map(t -> {
                if (t instanceof ParameterizedType) {
                    ParameterizedType type = (ParameterizedType) t;
                    if (AnnotationStream.class.getTypeName().equals(type.getRawType().getTypeName())) {
                        final Type[] actualTypeArguments = type.getActualTypeArguments();
                        if (actualTypeArguments.length == 0)
                            throw new RuntimeException("AnnotationStream must has generics");
                        return AnnotationStream.of(actualTypeArguments[0].getTypeName());
                    }
                } else if (t instanceof Class) {
                    return InstanceHolder.getCachedInstance((Class) t);
                }
                return null;
            }).toArray();
            method.invoke(instance, paraObjects);
        } catch (Throwable e) {
            PanguCore.getLogger().error("Unable to solve method " + method.toGenericString(), e);
        }
    }

    /**
     * invoke method like "public static void injectAnnotation(ASMDataTable data) ..."
     * <p>
     * 带有该注解的方法，必须是可见的，且是静态或者类的实例已存入 {@link InstanceHolder} （即使用了 {@link AutoWired} 的类）
     *
     * @since 1.0.2
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface StaticInvoke {
    }
}
