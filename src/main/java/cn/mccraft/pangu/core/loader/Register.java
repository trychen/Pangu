package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.buildin.IRegister;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
        Registering registering = object.getClass().getAnnotation(Registering.class);

        // get resource domain
        String domain = registering == null || registering.value().isEmpty() ? null : registering.value();

        // for all field to find registrable item
        // here is using getFields() which means that your item must be visible or it won't be register
        for (Field field : object.getClass().getFields()) {
            for (Annotation annotation : field.getAnnotations()) {
                // find RegisteringHandler anno
                RegisteringHandler handler = annotation.getClass().getAnnotation(RegisteringHandler.class);

                // ignore item without loader
                if (handler == null || handler.value().getClass().equals(IRegister.class)) continue;

                // get the cached instance of loader
                IRegister loader = getLoaderInstance(handler.value());

                Object item;
                try {
                    item = field.get(object);
                } catch (Exception e) {
                    // catch all exception to make sure no effect other item
                    PanguCore.getLogger().error("Unable to get item's instance: " + field.getName(), e);
                    continue;
                }

                loader.preRegister(new RegisteringItem<>(item, domain, annotation));
            }
        }
    }

    /**
     * loader's class to instance
     */
    public Map<Class<? extends IRegister>, Object> loadersInstanceMap = new HashMap<>();

    /**
     * get the cached instance
     *
     * @param loaderClass the loader's class
     * @return IRegister's instance, null if can't init
     */
    public IRegister getLoaderInstance(Class<? extends IRegister> loaderClass) {
        Object object = loadersInstanceMap.get(loaderClass);

        // check instance if exists
        if (object == null || !(object instanceof IRegister)) try {
            // new instance with reflection
            object = loaderClass.newInstance();
            // put to map
            loadersInstanceMap.put(loaderClass, object);
        } catch (Exception e) {
            // catch all exception to make sure no effect other loader
            PanguCore.getLogger().error("Unable to init loader: " + loaderClass, e);

            // return null to stop load item
            return null;
        }

        // cast to IRegister, here is safe
        return (IRegister) object;
    }
}