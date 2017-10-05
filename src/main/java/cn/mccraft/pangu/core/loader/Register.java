package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.buildin.IRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * The Registering System's Controller
 *
 * @author trychen
 * @since .3
 */
public enum Register {
    INSTANCE;

    /**
     * invoke item's loader
     *
     * @param object the instance of you items' class
     */
    public void register(Object object) {
        boolean isStatic = object instanceof Class;
        Class<?> parentClass = isStatic ? (Class) object : object.getClass();
        Object owner = isStatic ? null : object;

        Registering registering = parentClass.getAnnotation(Registering.class);

        // get resource domain
        String domain = registering == null || registering.value().isEmpty() ? PanguCore.MODID : registering.value();

        // for all field to find registrable item
        // here is using getFields() which means that your item must be visible or it won't be register
        for (Field field : parentClass.getFields()) {
            if (isStatic && !Modifier.isStatic(field.getModifiers())) continue;
            for (Annotation annotation : field.getAnnotations()) {
                // find RegisteringHandler anno
                RegisteringHandler handler = annotation.annotationType().getAnnotation(RegisteringHandler.class);

                // ignore item without loader
                if (handler == null|| IRegister.class.equals(handler.value()) || !IRegister.class.isAssignableFrom(handler.value())) continue;

                // get the cached instance of loader
                IRegister loader = getLoaderInstance(handler.value());

                Object item;
                try {
                    item = field.get(owner);
                } catch (Exception e) {
                    // catch all exception to make sure no effect other item
                    PanguCore.getLogger().error("Unable to get item's instance: " + field.getName(), e);
                    continue;
                }

                loader.preRegister(new RegisteringItem<>(field, item, domain, annotation));

            }
        }
    }

    /**
     * get the cached instance
     *
     * @param loaderClass the loader's class
     * @return IRegister's instance, null if can't init
     */
    public IRegister getLoaderInstance(Class<? extends IRegister> loaderClass) {
        Object object = InstanceHolder.getIntance(loaderClass);

        // check instance if exists
        if (object == null || !(object instanceof IRegister)) try {
            // new instance with reflection
            object = InstanceHolder.putInstance(loaderClass.newInstance());

            // subscribed to MinecraftForge.EVENT_BUS
            if (needSubscribedEventBus(loaderClass)) MinecraftForge.EVENT_BUS.register(object);
            // subscribed to MinecraftForge.EVENT_BUS
            if (needSubscribedLoad(loaderClass)) Proxy.INSTANCE.addLoader(object);
        } catch (Exception e) {
            // catch all exception to make sure no effect other loader
            PanguCore.getLogger().error("Unable to init loader: " + loaderClass, e);

            // return null to stop load item
            return null;
        }

        // cast to IRegister, here is safe
        return (IRegister) object;
    }

    public static boolean needSubscribedEventBus(Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) return true;
        }
        return false;
    }

    public static boolean needSubscribedLoad(Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Load.class)) return true;
        }
        return false;
    }

    @AnnotationInjector.StaticInvoke
    public static void injectAnnotation(ASMDataTable table) {
        table.getAll(Registering.class.getName())
                .stream()
                .map(ASMDataTable.ASMData::getClassName)
                .distinct()
                .forEach(it -> {
                    try {
                        INSTANCE.register(Class.forName(it));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }

}
