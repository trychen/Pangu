package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.SoundRegister;

import java.lang.annotation.*;

/**
 * auto load sound with base info
 *
 * @see SoundRegister
 * @since 1.0.0.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(SoundRegister.class)
public @interface RegSound {
    /**
     * ResourceLocation, only work when the field was null
     */
    String value() default "";
}
