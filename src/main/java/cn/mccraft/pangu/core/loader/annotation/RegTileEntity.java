package cn.mccraft.pangu.core.loader.annotation;

import cn.mccraft.pangu.core.loader.RegisteringHandler;
import cn.mccraft.pangu.core.loader.buildin.TileEntityRegister;

import java.lang.annotation.*;

/**
 * @see TileEntityRegister
 * @since 1.0.0.3
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@RegisteringHandler(TileEntityRegister.class)
public @interface RegTileEntity {
    /**
     * TileEntity Name
     */
    String value();
}
