package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.ItemRegister;

import java.lang.annotation.*;

/**
 * auto load item with base info
 *
 * @since 1.0.0.2
 */
@Documented
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
    boolean isRegisterModel() default true;

    /**
     * if auto create model automatically
     */
    boolean autoModel() default true;
}
