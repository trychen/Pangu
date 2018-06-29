package cn.mccraft.pangu.core.loader.annotation;


import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.RecipeRegister;

import java.lang.annotation.*;

/**
 * @see RecipeRegister
 * @since 1.0.0.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(RecipeRegister.class)
public @interface RegRecipe {
    /**
     * resource name
     */
    String value() default "";

    /**
     * if ignore this recipe when resource file exist
     */
    boolean autoIgnoreIfResourceExist() default true;
}
