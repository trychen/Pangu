package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.BlockRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.lang.annotation.*;

/**
 * 注册方块的注解
 *
 * @since 1.0.0.2
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(value = BlockRegister.class)
public @interface RegBlock {
    /**
     * 该参数将自动设置方块的registryName和unlocalizedName
     * The params to build registryName and unlocalizedName.
     */
    String[] value() default {};

    /**
     * 添加矿物词典
     * All {@link net.minecraftforge.oredict.OreDictionary} values to be registered.
     */
    String[] oreDict() default {};

    /**
     * 设置方块的ItemBlock类
     */
    Class<? extends ItemBlock> itemBlockClass() default ItemBlock.class;

    /**
     * 是否自动注册ItemBlock
     */
    boolean registerItemBlock() default true;

    /**
     * 是否自动注册渲染器
     */
    boolean registerRenderer() default true;
}
