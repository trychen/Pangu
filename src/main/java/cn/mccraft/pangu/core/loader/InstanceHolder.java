package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.util.ReflectUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public interface InstanceHolder {
    /**
     * The class to instance
     */
    Map<Class<?>, Object> loaderInstanceMap = new HashMap<>();

    static Object getIntance(@Nonnull Object object) {
        // check if object is a class to invoke static load
        boolean isStatic = object instanceof Class;

        // get the class with diff status
        Class<?> loaderClass = isStatic ? (Class<?>) object : object.getClass();

        Object cachedInstance = loaderInstanceMap.get(loaderClass);

        // try to find instance
        if (cachedInstance == null) {
            if (!isStatic) cachedInstance = object;
            if (cachedInstance == null) cachedInstance = ReflectUtils.getField(loaderClass, "instance");
            if (cachedInstance == null) cachedInstance = ReflectUtils.getField(loaderClass, "INSTANCE");
            if (cachedInstance == null) cachedInstance = ReflectUtils.invokeMethod(loaderClass, "getInstance");
            if (cachedInstance == null) cachedInstance = ReflectUtils.invokeMethod(loaderClass, "INSTANCE");
            if (cachedInstance != null) loaderInstanceMap.put(loaderClass, cachedInstance);
        }

        return cachedInstance;
    }

    static Object putInstance(Object object){
        loaderInstanceMap.put(object.getClass(), object);
        return object;
    }
}
