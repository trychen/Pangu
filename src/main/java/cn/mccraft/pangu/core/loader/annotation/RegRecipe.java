package cn.mccraft.pangu.core.loader.annotation;


import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.RecipeRegister;

import java.lang.annotation.*;

/**
 * You can use this annotation in an IRecipeProvider or IRecipe field.
 *
 * @see RecipeRegister
 * @since 1.0.0.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(RecipeRegister.class)
public @interface RegRecipe {
    /**
     * recipe group
     */
    String value() default "";
}
