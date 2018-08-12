package cn.mccraft.pangu.core.loader.annotation;


import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.RecipeRegister;

import java.lang.annotation.*;

/**
 * Register {@code IRecipe} or invoke {@code RecipeProvider#createRecipes()} automatically
 * You can use this annotation in an {@code RecipeProvider} or {@code IRecipe} field.
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
