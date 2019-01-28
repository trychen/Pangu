package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.transformer.RemoteTransformer;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.*;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.MethodAccessor;
import com.trychen.bytedatastream.ByteSerialization;
import lombok.Getter;
import lombok.experimental.Delegate;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Load
public class RemoteHandler {
    private static Map<String, CachedRemoteMessage> messages = new HashMap<>();

    public static boolean send(String messageType, Object[] objects) {
        // 是否在正在运行内置服务器
        if (Games.isIntegratedServer()) return false;

        // 获取消息
        CachedRemoteMessage packet = messages.get(messageType);

        // 空检测
        Objects.requireNonNull(packet, "Couldn't find any cached @Remote message");

        // Side 吻合时直接运行
        if (packet.getSide() == Sides.currentThreadSide()) return false;

        try {
            if (packet.isWithEntityPlayer()) {
                // 带 EntityPlayer 首参数
                Object[] data = Arrays.copyOfRange(objects, 1, objects.length);

                byte[] bytes = ByteSerialization.serialize(data, packet.getActualParameterTypes());
                ByteMessage byteMessage = (ByteMessage) packet.getMessageClass().newInstance();
                byteMessage.setBytes(bytes);
                if (packet.getSide() == Side.SERVER)
                    packet.network.sendToServer(byteMessage);
                else if (packet.getSide() == Side.CLIENT)
                    packet.network.sendTo(byteMessage, (EntityPlayerMP) objects[0]);
            } else {
                // 不带 EntityPlayer 参数
                byte[] bytes = ByteSerialization.serialize(objects, packet.getActualParameterTypes());
                ByteMessage byteMessage = (ByteMessage) packet.messageClass.newInstance();
                byteMessage.setBytes(bytes);
                if (packet.getSide() == Side.SERVER)
                    packet.network.sendToServer(byteMessage);
                else if (packet.getSide() == Side.CLIENT)
                    packet.network.sendToAll(byteMessage);
            }
        } catch (Exception e) {
            PanguCore.getLogger().error("Error while sending @Remote info", e);
        }
        return true;
    }

    @Load
    public static void registerMessages() {
        RemoteTransformer.messages.forEach(RemoteHandler::registerMessage);
    }

    public static void registerMessage(RemoteTransformer.RemoteMessage message) {
        try {
            CachedRemoteMessage cached = new CachedRemoteMessage(message);
            messages.put(message.getMessageClassName(), cached);
            cached.getNetwork().registerMessage(new MessageHandler(cached), cached.getMessageClass(), cached.getId(), cached.getSide());
            PanguCore.getLogger().info("Registered @Remote message from method " + cached.getMethod().toGenericString());
        } catch (Exception e) {
            PanguCore.getLogger().error("Couldn't register message", e);
        }
    }

    public static class MessageHandler implements IMessageHandler<ByteMessage, IMessage> {
        private CachedRemoteMessage cached;

        public MessageHandler(CachedRemoteMessage cached) {
            this.cached = cached;
        }

        @Override
        public IMessage onMessage(ByteMessage message, MessageContext ctx) {
            try {
                Object[] deserialize = ByteSerialization.deserialize(message.getBytes(), cached.getActualParameterTypes());
                if (cached.isWithEntityPlayer()) {
                    var objects = new Object[cached.getMethodArgs().length];
                    for (int i = 0; i < deserialize.length; i++) {
                        objects[i + 1] = deserialize[i];
                    }

                    if (Sides.isClient()) {
                        objects[0] = Games.player();
                    } else {
                        objects[0] = ctx.getServerHandler().player;
                    }

                    Threads.submit(
                            Try.safe(
                                    () -> cached.getMethodAccessor().invoke(cached.isStatic() ? null : InstanceHolder.getInstance(cached.getOwner()), objects),
                                    "Unable to handle @Remote for " + cached.messageClass.toGenericString()
                            ),
                            cached.isSync()
                    );
                } else {
                    Threads.submit(
                            Try.safe(
                                    () -> cached.getMethodAccessor().invoke(cached.isStatic() ? null : InstanceHolder.getInstance(cached.getOwner()), deserialize),
                                    "Unable to handle @Remote for " + cached.messageClass.toGenericString()
                            ),
                            cached.isSync()
                    );
                }
            } catch (Exception e) {
                PanguCore.getLogger().error("Unable to handle @Remote for " + cached.messageClass.toGenericString(), e);
            }
            return null;
        }
    }

    public static class CachedRemoteMessage {
        @Getter
        private Class messageClass;

        @Getter
        private Class[] methodArgs;

        @Getter
        @Delegate
        private RemoteTransformer.RemoteMessage nativeMessage;

        @Getter
        private boolean withEntityPlayer;

        @Getter
        private SimpleNetworkWrapper network;

        private MethodAccessor methodAccessor;
        private Method method;
        private Class owner;
        private Type[] actualParameterTypes;
        private boolean isStatic;

        public CachedRemoteMessage(RemoteTransformer.RemoteMessage remoteMessage) throws Exception {
            this.messageClass = PanguClassLoader.getInstance().defineClass(remoteMessage.messageClassName, remoteMessage.messageClassBytes);
            this.methodArgs = ReflectUtils.fromTypes(remoteMessage.methodArgTypes);
            this.network = Network.getNetworkWrapper(messageClass);
            this.withEntityPlayer = methodArgs.length > 0 && EntityPlayer.class.isAssignableFrom(methodArgs[0]);
            this.nativeMessage = remoteMessage;

            // clean useless data
            remoteMessage.messageClassBytes = null;
        }

        public Class getOwner() {
            if (owner == null) {
                owner = ReflectUtils.forName(nativeMessage.ownerClass);
            }
            return owner;
        }

        public MethodAccessor getMethodAccessor() throws Exception {
            if (methodAccessor == null) {
                var method = getMethod();
                this.methodAccessor = FastReflection.create(method);
                this.isStatic = Modifier.isStatic(method.getModifiers());
            }
            return methodAccessor;
        }

        public Method getMethod() throws NoSuchMethodException {
            if (method == null) {
                method = getOwner().getDeclaredMethod(nativeMessage.methodName, methodArgs);
            }
            return method;
        }

        public Type[] getActualParameterTypes() throws NoSuchMethodException {
            if (actualParameterTypes == null) {
                var method = getMethod();
                var parameterTypes = method.getGenericParameterTypes();
                if (isWithEntityPlayer()) {
                    this.actualParameterTypes = Arrays.copyOfRange(parameterTypes, 1, parameterTypes.length);
                } else this.actualParameterTypes = parameterTypes;
            }
            return actualParameterTypes;
        }

        public boolean isStatic() throws Exception {
            if (methodAccessor == null) getMethodAccessor();
            return isStatic;
        }
    }
}
