package cn.mccraft.pangu.core.network;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BindKeyMessage {
    /**
     * The message key
     */
    String value();
    /**
     * The side receiving message
     */
    Side side() default Side.SERVER;
}
