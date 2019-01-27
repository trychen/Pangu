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
    /**
     * Message id
     */
    int value();

    /**
     * The side to exec the method
     */
    Side side() default Side.SERVER;

    /**
     * Execute the method in "minecraft thread" instead "netty io thread"
     */
    boolean sync() default true;

    /**
     * Execute the method in both side
     */
    boolean also() default false;
}
