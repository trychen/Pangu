package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.PotionRegister;

import java.lang.annotation.*;

/**
 * @author trychen
 * @since 1.0.6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(PotionRegister.class)
public @interface RegPotion {
    /**
     * The params to build registryName and unlocalizedName,
     * using {@link cn.mccraft.pangu.core.util.NameBuilder}.
     * if you set this to empty, register will use
     * {@link cn.mccraft.pangu.core.util.NameBuilder#apart(String)}
     * to apart the field's name as the item's name.
     */
    String[] value() default {};
}
