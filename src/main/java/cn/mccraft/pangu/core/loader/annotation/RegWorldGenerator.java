package cn.mccraft.pangu.core.loader.annotation;

import java.lang.annotation.*;

/**
 * @see cn.mccraft.pangu.core.loader.buildin.WorldGeneratorRegister
 * @since 1.0.0.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegWorldGenerator {
}
