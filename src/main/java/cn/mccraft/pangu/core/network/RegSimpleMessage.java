package cn.mccraft.pangu.core.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.*;

/**
 * @see SimpleMessageRegister
 * @since 1.0.5
 * @author trychen
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegSimpleMessage {
    /**
     * Message context
     */
    Class<? extends IMessage> message();

    /**
     *
     * @return
     */
    int id();

    /**
     * The side receiving message
     */
    Side side() default Side.SERVER;
}
