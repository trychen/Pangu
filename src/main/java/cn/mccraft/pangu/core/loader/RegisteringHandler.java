package cn.mccraft.pangu.core.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark register for Reg- anno
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface RegisteringHandler {
    Class<? extends IRegister> value() default IRegister.class;
}
