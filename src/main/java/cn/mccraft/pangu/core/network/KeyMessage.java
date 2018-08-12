package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.MinecraftThreading;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.MethodAccessor;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cn.mccraft.pangu.core.network.Network.*;

/**
 * A simple way to send a notice to server
 *
 * @since 1.0.5
 * @author trychen
 */
public interface KeyMessage {
    @Load(LoaderState.INITIALIZATION)
    static void registerMessageHandler(){
        PanguCore.getNetwork().registerMessage(new Handler(Side.SERVER), Content.class, SERVER_KEY_MESSAGE_ID, Side.SERVER);
        PanguCore.getNetwork().registerMessage(new Handler(Side.CLIENT), Content.class, CLIENT_KEY_MESSAGE_ID, Side.CLIENT);
    }

    static void send(String key) {
        PanguCore.getNetwork().sendToServer(new Content(key));
    }

    static void send(String key, EntityPlayerMP entityPlayer) {
        PanguCore.getNetwork().sendTo(new Content(key), entityPlayer);
    }

    Map<String, Consumer<MessageContext>> name2ReceiverForClient = new HashMap<>();
    Map<String, Consumer<MessageContext>> name2ReceiverForServer = new HashMap<>();

    static Consumer<MessageContext> register(@Nonnull String name, @Nonnull Consumer<MessageContext> receiver, Side side){
        return (side.isServer() ? name2ReceiverForServer : name2ReceiverForClient).put(name, receiver);
    }

    @AnnotationInjector.StaticInvoke
    static void bind(AnnotationStream<BindKeyMessage> stream) {
        stream.methodStream()
                .forEach(method -> {
                    boolean hasMessageContext = method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(MessageContext.class);
                    if (method.getParameterCount() != 0 && !hasMessageContext) {
                        PanguCore.getLogger().error(String.format("Unexpected parameter while binding key message for method: %s", method.toGenericString()), new IllegalArgumentException());
                        return;
                    }

                    // check if there is an instance to invoke
                    Object instance = null;
                    if (!Modifier.isStatic(method.getModifiers()) && (instance = InstanceHolder.getCachedInstance(method.getDeclaringClass())) == null) {
                        PanguCore.getLogger().error("Unable to find any instance to bind key message for method " + method.toString(), new NullPointerException());
                        return;
                    }

                    MethodAccessor methodAccessor;
                    try {
                        methodAccessor = FastReflection.create(method);
                    } catch (Exception e) {
                        PanguCore.getLogger().error(String.format("Unable to create AsmMethodAccessor for binding key message (method: %s)", method.toGenericString()), e);
                        return;
                    }

                    // get annotation info
                    BindKeyMessage bindKeyMessage = method.getAnnotation(BindKeyMessage.class);

                    Object finalInstance = instance;

                    register(bindKeyMessage.value(), messageContext -> {
                        try {
                            if (!hasMessageContext)
                                methodAccessor.invoke(finalInstance);
                            else
                                methodAccessor.invoke(finalInstance, messageContext);
                        } catch (Exception e) {
                            PanguCore.getLogger().error("Exception while invoking key message binding", e);
                        }
                    }, bindKeyMessage.side());

                    PanguCore.getLogger().info("Registered KeyMessage Binder for method " + method.toGenericString());
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
            MinecraftThreading.submit(() -> {
                Consumer<MessageContext> receiver = (side.isServer() ? name2ReceiverForServer : name2ReceiverForClient).get(message.key);
                if (receiver != null) {
                    receiver.accept(ctx);
                } else {
                    PanguCore.getLogger().warn("Received an unregistered packet with key " + message.key);
                }
            });
            return null;
        }
    }

    /**
     * A input message which storage none of information
     */
    class Content implements IMessage {
        private String key;

        /**
         * No parameter constructor for message to message codec
         */
        public Content() {
        }

        public Content(String key) {
            this.key = key;
        }

        /**
         * Convert from the supplied buffer into your specific message type
         */
        @Override
        public void fromBytes(ByteBuf buf) {
            key = ByteBufUtils.readUTF8String(buf);
        }

        /**
         * Deconstruct your message into the supplied byte buffer
         */
        @Override
        public void toBytes(ByteBuf buf) {
            ByteBufUtils.writeUTF8String(buf, key);
        }
    }
}
