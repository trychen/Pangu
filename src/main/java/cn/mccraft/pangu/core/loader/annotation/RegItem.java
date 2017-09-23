package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.ItemRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * auto load
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(ItemRegister.class)
public @interface RegItem {
    /**
     * The params to build registryName and unlocalizedName,
     * using {@link cn.mccraft.pangu.core.util.NameBuilder}
     */
    String[] value();

    /**
     * All {@link net.minecraftforge.oredict.OreDictionary} values to be registered.
     */
    String[] oreDict() default {};

    boolean isRegisterRender() default true;
}
