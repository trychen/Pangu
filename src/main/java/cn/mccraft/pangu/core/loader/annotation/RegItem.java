package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.ItemRegister;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register {@code Item} automatically.
 * You can only use this anno in a {@code Item} field.
 *
 * @see ItemRegister
 * @since 1.0.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(ItemRegister.class)
public @interface RegItem {
    /**
     * The params to build registryName and unlocalizedName,
     * using {@link cn.mccraft.pangu.core.util.NameBuilder}.
     * if you set this to empty, register will use
     * {@link cn.mccraft.pangu.core.util.NameBuilder#apart(String)}
     * to apart the field's name as the item's name.
     */
    String[] value() default {};

    /**
     * All {@link net.minecraftforge.oredict.OreDictionary} values to be registered.
     */
    String[] oreDict() default {};

    /**
     * if register model automatically
     */
    boolean registerModel() default true;

    /**
     * if auto create model automatically
     */
    @Deprecated
    boolean autoModel() default false;
}
