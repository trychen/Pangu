package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import lombok.val;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
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

    interface SimpleMessageRegister {
        @AnnotationInjector.StaticInvoke
        static void bind(AnnotationStream<RegSimpleMessage> stream) {
            stream.typeStream().forEach(clazz -> {
                try {
                    // get meta
                    RegSimpleMessage anno = clazz.getAnnotation(RegSimpleMessage.class);

                    // check class
                    if (!IMessageHandler.class.isAssignableFrom(clazz)) {
                        PanguCore.getLogger().error("You can only use @RegSimpleMessage to an IMessageHandler class, but given " + clazz.toGenericString(), new IllegalArgumentException());
                        return;
                    }
                    val channel = Network.getNetworkWrapper(clazz);
                    if (channel == null) return;

                    // init handler
                    //noinspection unchecked
                    val messageHandler = (IMessageHandler) InstanceHolder.getOrNewInstance(clazz);

                    //noinspection unchecked
                    channel.registerMessage(messageHandler, anno.message(), anno.id(), anno.side());

                    PanguCore.getLogger().info("Registered SimpleMessage handler for class " + clazz.toGenericString());
                } catch (Exception e) {
                    PanguCore.getLogger().error("Unexpected error while registering SimpleMessage for class" + clazz.toGenericString(), e);
                }
            });
        }
    }

}
