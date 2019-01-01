package cn.mccraft.pangu.core.network;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.*;

/**
 * @since 1.4
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
    int value();

    /**
     * The side to exec the method
     */
    Side side() default Side.SERVER;


    boolean sync() default true;
}
