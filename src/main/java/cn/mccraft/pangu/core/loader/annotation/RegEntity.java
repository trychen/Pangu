package cn.mccraft.pangu.core.loader.annotation;

import java.lang.annotation.*;

/**
 * You can only use this anno in a {@link net.minecraftforge.fml.common.registry.EntityEntry} field.
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
