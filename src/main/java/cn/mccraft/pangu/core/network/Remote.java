package cn.mccraft.pangu.core.network;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @since 1.4
 */
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
