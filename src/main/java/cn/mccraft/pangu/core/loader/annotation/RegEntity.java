package cn.mccraft.pangu.core.loader.annotation;

import java.lang.annotation.*;

/**
 * Register {@code EntityEntry} automatically.
 * You can only use this anno in a {@code EntityEntry} field.
 *
 * @see cn.mccraft.pangu.core.loader.buildin.EntityRegister
 * @since 1.0.2
 * @author trychen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegEntity {
    /**
     * @return true if you want to add a spawn egg
     */
    boolean addEgg() default false;

    /**
     * Base color of the egg
     */
    int eggPrimaryColor() default 0xFFFFFF;

    /**
     * Color of the egg spots
     */
    int eggSecondaryColor() default 0x000000;
}
