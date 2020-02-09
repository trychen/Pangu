package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.EntityRegister;

import java.lang.annotation.*;

/**
 * Register {@code Entity} automatically.
 *
 * @see cn.mccraft.pangu.core.loader.buildin.EntityRegister
 * @since 1.0.2
 * @author trychen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RegisteringHandler(EntityRegister.class)
public @interface RegEntity {
    String value();
    /**
     * The range at which MC will send tracking updates
     */
    int trackingRange() default 64;

    /**
     * The frequency of tracking updates
     */
    int updateFrequency() default 20;

    /**
     * Whether to send velocity information packets as well
     */
    boolean sendsVelocityUpdates() default true;

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
