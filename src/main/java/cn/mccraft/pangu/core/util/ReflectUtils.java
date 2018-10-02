package cn.mccraft.pangu.core.util;

import cn.mccraft.pangu.core.PanguCore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Reflection Utils without any exception.
 *
 * @since 1.0.0.2
 * @author trychen
 */
public interface ReflectUtils {
    /**
     * Set field value
     *
     * @param ownerClass declaring class
     * @param owner the object whose field should be modified
     * @param name the name of field
     * @param object value the new value for the field of {@code obj} being modified
     * @param breakPrivate whether set field accessible
     *
     * @return true if succeed
     */
    static boolean setField(Class ownerClass, Object owner, String name, Object object, boolean breakPrivate) {
        try {
            // resolve break private
            Field field;
            try {
                field = breakPrivate ? ownerClass.getDeclaredField(name) : ownerClass.getField(name);
            } catch (NoSuchFieldException e){
                // check no field
                return false;
            }

            // break access
            if (!field.isAccessible()) field.setAccessible(true);

            field.set(owner, object);
            return true;
        } catch (Exception e) {
            PanguCore.getLogger().error(
                    String.format(
                            "Unable to set field in %s#%s typed %s, breakPrivate?%b",
                            ownerClass.getName(),
                            name,
                            object,
                            breakPrivate
                    ), e);
            return false;
        }
    }

    static boolean setField(Object owner, String name, Object obj) {
        return setField(owner.getClass(), owner, name, obj, false);
    }

    static boolean setField(Class clazz, String name, Object obj) {
        return setField(clazz, null, name, obj, false);
    }

    /**
     * Get field value
     *
     * @param ownerClass declaring class
     * @param owner the object whose field should be modified
     * @param name the name of field
     * @param typeCheck check field type
     * @param breakPrivate whether set field accessible
     *
     * @return null if failed
     */
    @Nullable
    static <T> T getField(@Nonnull Class ownerClass, Object owner, @Nonnull String name, Class<T> typeCheck, boolean breakPrivate) {
        try {
            // resolve break private
            Field field;
            try {
                field = breakPrivate ? ownerClass.getDeclaredField(name) : ownerClass.getField(name);
            } catch (NoSuchFieldException e){
                // check no field
                return null;
            }
            // check type
            if (typeCheck != null && !typeCheck.isAssignableFrom(field.getType())) return null;

            // break access
            if (!field.isAccessible()) field.setAccessible(true);

            Object object = field.get(owner);

            //noinspection unchecked
            return (T) object;
        } catch (Exception e) {
            PanguCore.getLogger().error(
                    String.format(
                            "Unable to get field in %s#%s typed %s, breakPrivate?%b",
                            ownerClass.getName(),
                            name,
                            typeCheck,
                            breakPrivate
                    ), e);
        }
        return null;
    }
    @Nullable
    static <T> T getField(Object owner, String name, Class<T> typeCheck) {
        return getField(owner.getClass(), owner, name, typeCheck, false);
    }
    @Nullable
    static <T> T getField(Class ownerClass, String name, Class<T> typeCheck) {
        return getField(ownerClass, null, name, typeCheck, false);
    }
    @Nullable
    static Object getField(Object owner, String name) {
        return getField(owner.getClass(), owner, name, null, false);
    }
    @Nullable
    static Object getField(Class ownerClass, String name) {
        return getField(ownerClass, null, name, null, false);
    }

    /**
     * invoke method
     *
     * @param ownerClass method's owner
     * @param owner the invoker
     * @param name the name of method
     * @param returnTypeCheck the type of method's return, null for void
     * @param breakPrivate if set accessible to true
     * @param parameters the parameters to invoke
     */
    @Nullable
    static <T> T invokeMethod(Class ownerClass, Object owner, String name, Class<T> returnTypeCheck, boolean breakPrivate, Object... parameters) {
        try {
            Class<?>[] parameterTypes = toTypes(parameters);

            // resolve break private
            Method method;
            try {
                //noinspection unchecked
                method = breakPrivate ? ownerClass.getDeclaredMethod(name, parameterTypes) : ownerClass.getMethod(name, parameterTypes);
            } catch (NoSuchMethodException e){
                return null;
            }

            // check type
            if (returnTypeCheck != null && !returnTypeCheck.isAssignableFrom(method.getReturnType())) return null;

            // break access
            if (breakPrivate && !method.isAccessible()) method.setAccessible(true);

            Object object = method.invoke(owner, parameters);
            //noinspection unchecked
            return (T) object;
        } catch (Exception e) {
            PanguCore.getLogger().error(
                    String.format(
                            "Unable to get field in %s#method %s, typed %s, breakPrivate?%b",
                            ownerClass,
                            name,
                            returnTypeCheck,
                            breakPrivate
                    ), e);
        }
        return null;
    }

    static <T> T invokeMethod(Annotation anno, String name, Class<T> returnTypeCheck, boolean breakPrivate, Object... parameters) {
        return invokeMethod(anno.annotationType(), anno, name,returnTypeCheck, breakPrivate, parameters);
    }
    static <T> T invokeMethod(Class ownerClass, String name, Class<T> returnTypeCheck, boolean breakPrivate, Object... parameters) {
        return invokeMethod(ownerClass, null, name,returnTypeCheck, breakPrivate, parameters);
    }
    static <T> T invokeMethod(Object owner, String name, Class<T> returnTypeCheck, boolean breakPrivate, Object... parameters) {
        return invokeMethod(owner.getClass(), owner, name,returnTypeCheck, breakPrivate, parameters);
    }

    static <T> T invokeMethod(Class ownerClass, String name, Class<T> returnTypeCheck) {
        return invokeMethod(ownerClass, null, name,returnTypeCheck, false);
    }
    static <T> T invokeMethod(Object owner, String name, Class<T> returnTypeCheck) {
        return invokeMethod(owner.getClass(), owner, name, returnTypeCheck, false);
    }
    static <T> T invokeMethod(Annotation anno, String name, Class<T> returnTypeCheck) {
        return invokeMethod(anno.annotationType(), anno, name, returnTypeCheck, false);
    }

    static <T> T invokeMethod(Object owner, String name) {
        return invokeMethod(owner, name, null);
    }
    static <T> T invokeMethod(Class ownerClass, String name) {
        return invokeMethod(ownerClass, name, null);
    }
    static <T> T invokeMethod(Annotation anno, String name) {
        return invokeMethod(anno.annotationType(), anno, name, null, false);
    }

    /**
     * transforms parameters array to class array
     */
    static Class<?>[] toTypes(Object... parameters){
        return Arrays.stream(parameters).map(Object::getClass).toArray(Class[]::new);
    }

    /**
     * Get a instance of class by class name
     *
     * @return null if failed
     */
    static Class<?> forName(String name){
        try {
            return Class.forName(name);
        } catch (Exception e) {
            PanguCore.getLogger().error(e);
        }
        return null;
    }

    /**
     * Creates a new instance of the class
     *
     * @param name the name of the class
     * @return null if failed
     */
    @Nullable
    static Object forInstance(@Nonnull String name){
        try {
            //noinspection ConstantConditions
            return Class.forName(name).newInstance();
        } catch (Exception e) {
            PanguCore.getLogger().error(e);
        }
        return null;
    }

    /**
     * Creates a new instance of the class
     *
     * @return null if failed
     */
    @Nullable
    static <T> T forInstance(@Nonnull Class<T> clazz){
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            PanguCore.getLogger().error(e);
        }
        return null;
    }

    /**
     * Creates a new instance of the class with construction
     *
     * @param parameter the parameter array
     * @return null if failed
     */
    @Nullable
    static <T> T forInstance(@Nonnull Class<T> clazz, Object... parameter){
        try {
            return clazz.getConstructor(toTypes(parameter)).newInstance(parameter);
        } catch (Exception e) {
            PanguCore.getLogger().error(e);
        }
        return null;
    }
}
