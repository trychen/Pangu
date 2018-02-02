package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.SoundRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * auto load item with base info
 *
 * @since 1.0.0.4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(SoundRegister.class)
public @interface RegSound {
    /**
     * ResourceLocation, only work when the field was null
     */
    String value() default "";
}
