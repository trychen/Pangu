package cn.mccraft.pangu.core.asm.dev;

import java.lang.annotation.*;

/**
 * The element with this annotation won't exist in production env.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface DevOnly {
}
