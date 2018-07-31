package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.BlockRegister;
import net.minecraft.item.ItemBlock;

import java.lang.annotation.*;

/**
 * Register {@code Block} automatically.
 * You can only use this anno in a {@code Block} field.
 *
 * @since 1.0.0.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(value = BlockRegister.class)
public @interface RegBlock {
    /**
     * The params to build registryName and unlocalizedName.
     */
    String[] value() default {};

    /**
     * All {@link net.minecraftforge.oredict.OreDictionary} values to be registered.
     */
    String[] oreDict() default {};

    /**
     * Custom ItemBlock class for block
     */
    Class<? extends ItemBlock> itemBlockClass() default ItemBlock.class;

    /**
     * If register ItemBlock automatically
     */
    boolean registerItemBlock() default true;

    /**
     * Not implemented. If register Renderer automatically.
     */
    boolean registerRenderer() default true;
}
