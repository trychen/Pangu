package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ModDiscoverer;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * auto annotation discover
 */
public enum AnnotationInjector {
    INSTANCE;

    /**
     * invoke method like "public static void injectAnnotation(ASMDataTable.ASMData data) ..."
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface StaticInvoke {
    }

    /**
     * start solve all annotation injector
     */
    public void startSolveInjectors() {
        ModDiscoverer discoverer = getDiscoverer();

        Set<ASMDataTable.ASMData> allAutoWireds = discoverer.getASMTable().getAll(AutoWired.class.getName());
        Set<ASMDataTable.ASMData> allInvokers = discoverer.getASMTable().getAll(StaticInvoke.class.getName());

        allAutoWireds
                .stream()
                .filter(it -> it.getClassName().equals(it.getObjectName()))
                .forEach(it -> InstanceHolder.putInstance(ReflectUtils.forInstance(it.getClassName())));

        allAutoWireds
                .stream()
                .filter(it -> !it.getClassName().equals(it.getObjectName()))
                .forEach(data -> {
                    Class parentClass = ReflectUtils.forName(data.getClassName());
                    String annotationTarget = data.getObjectName();
                    Type type = (Type) data.getAnnotationInfo().get("value");
                    Class value = ReflectUtils.forName(type.getClassName() == null ? Object.class.getName() : type.getClassName());

                    Object object;
                    try {
                        Field field = parentClass.getDeclaredField(annotationTarget);
                        object = InstanceHolder.getIntance(type == null || Object.class.equals(value) ? field.getType() : value);
                    } catch (NoSuchFieldException e) {
                        PanguCore.getLogger().error("", e);
                        return;
                    }

                    if (object == null) {
                        PanguCore.getLogger().error("Couldn't found instance of " + parentClass + "#" + annotationTarget);
                        return;
                    }

                    ReflectUtils.setField(parentClass, InstanceHolder.getIntance(parentClass), annotationTarget, InstanceHolder.getIntance(parentClass), true);
                });

        for (ASMDataTable.ASMData load : allInvokers) {
            Class parentClass = ReflectUtils.forName(load.getClassName());
            String methodName = load.getObjectName().substring(0, load.getObjectName().indexOf('('));
            ReflectUtils.invokeMethod(parentClass, InstanceHolder.getIntance(parentClass), methodName, null, false, discoverer.getASMTable());
        }
    }

    private static ModDiscoverer discoverer;

    /**
     * get {@link Loader#discoverer} by {@link ReflectUtils}
     *
     * @return
     */
    public ModDiscoverer getDiscoverer() {
        if (discoverer == null) {
            discoverer = ReflectUtils.getField(Loader.class, Loader.instance(), "discoverer", ModDiscoverer.class, true);
        }
        return discoverer;
    }
}
