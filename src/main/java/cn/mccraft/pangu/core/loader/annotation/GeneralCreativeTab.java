package cn.mccraft.pangu.core.loader.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Could only use to a {@link net.minecraft.creativetab.CreativeTabs} field.
 * The field annotated will be automatically set value
 * (a {@link cn.mccraft.pangu.core.loader.buildin.CreativeTabRegister.PanguCreativeTab} instance)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneralCreativeTab {
    /**
     * the name of the creative tabs
     */
    String value();
}
