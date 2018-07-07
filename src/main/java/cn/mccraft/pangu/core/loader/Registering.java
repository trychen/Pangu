package cn.mccraft.pangu.core.loader;

import cn.mccraft.pangu.core.PanguCore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The class using this annotation can set special registering name or so
 * @author trychen
 * @since .3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Registering {
    /**
     * Resource Domain, could be empty if you had modified in "Reg-".
     *
     * @return resourceDomain
     */
    String value() default PanguCore.ID;
}
