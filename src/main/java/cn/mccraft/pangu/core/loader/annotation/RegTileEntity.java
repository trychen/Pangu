package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.buildin.TileEntityRegister;

import java.lang.annotation.*;

/**
 * @see TileEntityRegister
 * @since 1.0.0.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegTileEntity {
    /**
     * TileEntity Name
     */
    String value();
}
