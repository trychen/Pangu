package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.BlockRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注册方块的注解
 *
 * @since 1.0.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RegisteringHandler(BlockRegister.class)
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
    Class<? extends Item> itemClass() default ItemBlock.class;

    /**
     * 是否自动注册ItemBlock
     */
    boolean isRegisterItemBlock() default true;

    /**
     * 是否自动注册渲染器
     */
    boolean isRegisterRender() default true;
}
