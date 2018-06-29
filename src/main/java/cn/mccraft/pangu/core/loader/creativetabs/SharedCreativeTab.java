package cn.mccraft.pangu.core.loader.creativetabs;

import cn.mccraft.pangu.core.loader.creativetabs.CreativeTabRegister;

import java.lang.annotation.*;

/**
 * Indicates that a {@link net.minecraft.creativetab.CreativeTabs} field are
 * to be automatically set value with an instance from
 * {@link CreativeTabRegister#getTab(String)}.<br>
 * And for a type or field, all {@link net.minecraft.item.Item} and {@link net.minecraft.block.Block}
 * fields of it or the field annotated will be also automatically
 * set value with an instance from {@link CreativeTabRegister#getTab(String)}.<br>
 * Especially, if your field is non-static, {@link CreativeTabRegister} will try to
 * find any instance from {@link cn.mccraft.pangu.core.loader.InstanceHolder}
 *
 * @since 1.0.1
 * @author trychen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface SharedCreativeTab {
    /**
     * the name of the creative tabs
     */
    String value();
}
