package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.client.input.BindKeyPress;
import cn.mccraft.pangu.core.client.input.KeyBindingHelper;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple way to send a notice to server
 *
 * @since 1.0.0.4
 * @author trychen
 */
public interface KeyMessage {
    @Load(LoaderState.INITIALIZATION)
    static void registerMessageHandler(){
        PanguCore.getNetwork().registerMessage(new Handler(Side.SERVER), Content.class, Network.getNextID(), Side.SERVER);
        PanguCore.getNetwork().registerMessage(new Handler(Side.CLIENT), Content.class, Network.getNextID(), Side.CLIENT);
    }

    static void send(String key) {
        PanguCore.getNetwork().sendToServer(new Content(key));
    }

    static void send(String key, EntityPlayerMP entityPlayer) {
        PanguCore.getNetwork().sendTo(new Content(key), entityPlayer);
    }

    Map<String, Consumer<MessageContext>> name2Receiver = new HashMap<>();

    static Consumer<MessageContext> register(@Nonnull String name, @Nonnull Consumer<MessageContext> receiver){
        return name2Receiver.put(name, receiver);
    }

    @AnnotationInjector.StaticInvoke
    static void bind(AnnotationStream<BindKeyMessage> stream) {
        stream.methodStream()
                .filter(method -> method.getParameterCount() == 0)
                .forEach(method -> {
                    // check if there is an instance to invoke
                    if (!Modifier.isStatic(method.getModifiers()) && InstanceHolder.getCachedInstance(method.getDeclaringClass()) == null) {
                        PanguCore.getLogger().error("Unable to find any instance to bind key message for method " + method.toString(), new NullPointerException());
                        return;
                    }
                    // get annotation info
                    BindKeyMessage bindKeyPress = method.getAnnotation(BindKeyMessage.class);

                    // TODO:
//                    register(bindKeyPress.value())
                });
    }

    class Handler implements IMessageHandler<Content, Content> {
        private Side side;

        public Handler(Side side) {
            this.side = side;
        }

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx context
         * @return an optional return message
         */
        @Override
        public Content onMessage(Content message, MessageContext ctx) {
            Consumer<MessageContext> receiver = name2Receiver.get(message.key);
            if (receiver != null) {
                receiver.accept(ctx);
            } else {
                PanguCore.getLogger().warn("Client is trying to send an unregistered packet");
            }
            return message;
        }
    }

    /**
     * A input message which storage none of information
     */
    class Content implements IMessage {
        private String key;

        public Content(String key) {
            this.key = key;
        }

        /**
         * Convert from the supplied buffer into your specific message type
         *
         * @param buf
         */
        @Override
        public void fromBytes(ByteBuf buf) {
            int length = buf.getInt(0);
            key = buf.getCharSequence(1, length, StandardCharsets.UTF_8).toString();
        }

        /**
         * Deconstruct your message into the supplied byte buffer
         *
         * @param buf buffer
         */
        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(key.getBytes(StandardCharsets.UTF_8).length);
            buf.writeCharSequence(key, StandardCharsets.UTF_8);
        }
    }
}
