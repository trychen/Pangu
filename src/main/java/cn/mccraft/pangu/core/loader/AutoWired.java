package cn.mccraft.pangu.core.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Auto new instance or auto set value of field
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
