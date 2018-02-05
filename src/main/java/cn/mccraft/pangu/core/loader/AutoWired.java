package cn.mccraft.pangu.core.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auto new shared instance or auto set value of field.
 * If you use @AutoWired to a class, the class will be auto new
 * instance before preinit and storage to {@link InstanceHolder}.
 * it means that you can find your class's instance in it,
 * or using @AutoWired to a field typed class that @AutoWired
 * (must be static or parent's class is using @AutoWired also)
 * that it will be auto set to the instance.
 * Example:
 * <pre>
 *     <code>
 *          public class Tools {
 *              @AutoWired
 *              public static Hello hello;
 *
 *              @AutoWired
 *              public static class Hello {
 *              }
 *         }
 *     </code>
 * </pre>
 * Before preinit, class Hello will be auto created and put
 * to {@link InstanceHolder}, what's more hello will be auto set to
 * the shared instance of class Hello
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface AutoWired {
    /**
     * the class your want to wire to.
     * No effect to class, only for field.
     */
    Class<?> value() default Object.class;
}
