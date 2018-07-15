package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.util.ReflectUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * instance holder to storage some class's instance
 */
public interface InstanceHolder {
    /**
     * Store all mod instance
     */
    static void storeAllModInstance() {
        Loader.instance().getModList().stream().filter(Objects::nonNull).map(ModContainer::getMod).forEach(InstanceHolder::putInstance);
    }
    /**
     * The class to instance
     */
    Map<Class<?>, Object> loaderInstanceMap = new HashMap<>();

    /**
     * Getting the instance of class.
     * Firstly, it will find in {@link InstanceHolder##loaderInstanceMap}.
     * Secondly, it will find in class's fields named "instance", "INSTANCE".
     * or method like "getInstance", "instance".
     * At last storage to {@link InstanceHolder##loaderInstanceMap} if found instance or return null
     *
     * @param object class or object your want to get or storage
     * @return null if no saved instance
     */
    static Object getInstance(@Nonnull Object object) {
        // check if object is a class to invoke static load
        boolean isStatic = object instanceof Class;

        // get the class with diff status
        Class<?> loaderClass = isStatic ? (Class<?>) object : object.getClass();

        Object cachedInstance = loaderInstanceMap.get(loaderClass);

        // try to find instance
        if (cachedInstance == null) {
            // insert the provided object
            if (!isStatic) cachedInstance = object;

            // try to find instance in class
            if (cachedInstance == null) cachedInstance = ReflectUtils.getField(loaderClass, "instance");
            if (cachedInstance == null) cachedInstance = ReflectUtils.getField(loaderClass, "INSTANCE");
            if (cachedInstance == null) cachedInstance = ReflectUtils.invokeMethod(loaderClass, "getInstance");
            if (cachedInstance == null) cachedInstance = ReflectUtils.invokeMethod(loaderClass, "instance");
            if (cachedInstance != null) loaderInstanceMap.put(loaderClass, cachedInstance);
        }

        return cachedInstance;
    }

    static Object getCachedInstance(Class clazz) {
        if (clazz == null) throw new IllegalArgumentException();
        return loaderInstanceMap.get(clazz);
    }

    static <T> T getOrNewInstance(@Nonnull Class<T> clazz) {
        Object object = InstanceHolder.getInstance(clazz);
        if (object == null) {
            return InstanceHolder.putInstance(ReflectUtils.forInstance(clazz));
        } else if (clazz.isInstance(object)) {
            return (T) object;
        } else {
            loaderInstanceMap.remove(clazz);
            return getOrNewInstance(clazz);
        }
    }

    /**
     * Putting the instance to {@link InstanceHolder##loaderInstanceMap} and return it
     *
     * @param object the instance your gotta storage
     * @return the instance you given
     */
    static <T> T putInstance(@Nonnull T object) {
        loaderInstanceMap.put(object.getClass(), object);
        return object;
    }

    /**
     * Try to find instance for class
     *
     * @return true while found, false while couldn't
     */
    static boolean searchInstance(Class<?> clazz) {
        return loaderInstanceMap.containsKey(clazz);
    }

    static Object getObject(Field field) throws IllegalAccessException {
        if (!field.isAccessible()) field.setAccessible(true);
        if (Modifier.isStatic(field.getModifiers())) {
            return field.get(null);
        } else {
            Object owner = InstanceHolder.getInstance(field.getDeclaringClass());
            if (owner == null) {
                throw new IllegalAccessException("Couldn't find instance to get field");
            }
            return field.get(InstanceHolder.getInstance(field.getDeclaringClass()));
        }
    }

    /**
     * Try to set the object to the field.
     * If the field is static, the method will directly set value using {@link Field#set(Object, Object)}.
     * But if the field is non-static, the method will try to find any
     * instance of the parent class from {@link InstanceHolder#getInstance(Object)}.
     *
     * @param field the field to set
     * @param object instance
     * @throws IllegalAccessException if couldn't find any instance to set field while static
     */
    static void setObject(Field field, Object object) throws IllegalAccessException {
        if (!field.isAccessible()) field.setAccessible(true);
        if (Modifier.isStatic(field.getModifiers())) {
            field.set(null, object);
        } else {
            Object owner = InstanceHolder.getInstance(field.getDeclaringClass());
            if (owner == null) {
                throw new IllegalAccessException("Couldn't find instance to set field");
            }
            field.set(InstanceHolder.getInstance(field.getDeclaringClass()), object);
        }
    }
}
