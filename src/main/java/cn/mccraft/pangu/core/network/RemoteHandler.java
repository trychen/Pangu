package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.asm.PanguPlugin;
import cn.mccraft.pangu.core.asm.transformer.RemoteTransformer;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.MinecraftThreading;
import cn.mccraft.pangu.core.util.PanguClassLoader;
import cn.mccraft.pangu.core.util.ReflectUtils;
import cn.mccraft.pangu.core.util.data.ByteSerialization;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.MethodAccessor;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RemoteHandler {
    private static Map<String, CachedRemoteMessage> messages = new HashMap<>();

    public static boolean send(String messageType, Object[] objects) {
        if (MinecraftThreading.commonSide() == Side.CLIENT && Minecraft.getMinecraft().isIntegratedServerRunning())
            return false;
        CachedRemoteMessage cached = messages.get(messageType);
        Objects.requireNonNull(cached, "Couldn't find any cached @Remote message");
        if (cached.nativeMessage.side == MinecraftThreading.currentThreadSide()) return false;
        try {
            if (cached.withEntityPlayer) {
                Object[] datas = Arrays.copyOfRange(objects, 1, objects.length);
                byte[] bytes = ByteSerialization.INSTANCE.serialize(datas);
                ByteMessage byteMessage = (ByteMessage) cached.messageClass.newInstance();
                byteMessage.setBytes(bytes);
                if (cached.nativeMessage.side == Side.SERVER)
                    cached.network.sendToServer(byteMessage);
                else if (cached.nativeMessage.side == Side.CLIENT)
                    cached.network.sendTo(byteMessage, (EntityPlayerMP) objects[0]);
            } else {
                byte[] bytes = ByteSerialization.INSTANCE.serialize(objects);
                ByteMessage byteMessage = (ByteMessage) cached.messageClass.newInstance();
                byteMessage.setBytes(bytes);
                if (cached.nativeMessage.side == Side.SERVER)
                    cached.network.sendToAll(byteMessage);
                else if (cached.nativeMessage.side == Side.CLIENT)
                    cached.network.sendToServer(byteMessage);
            }
        } catch (Exception e) {
            PanguCore.getLogger().error("Error while sending @Remote info", e);
        }
        return true;
    }

    @Load
    public static void registerMessages() {
        RemoteTransformer.messages.forEach(RemoteHandler::registerMessage);
        RemoteTransformer.messages = null;
    }

    public static void registerMessage(RemoteTransformer.RemoteMessage message) {
        try {
            CachedRemoteMessage cached = new CachedRemoteMessage(message);
            messages.put(message.messageClassName, cached);
            cached.network.registerMessage(new MessageHandler(cached), cached.messageClass, cached.id, cached.nativeMessage.side);
        } catch (Exception e) {
            PanguPlugin.getLogger().error("Couldn't register message", e);
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
                if (cached.withEntityPlayer) {
                    Object[] objects = new Object[cached.methodArgs.length];
                    Object[] deserialize = ByteSerialization.INSTANCE.deserialize(message.getBytes(), Arrays.copyOfRange(cached.methodArgs, 1, cached.methodArgs.length));
                    for (int i = 0; i < deserialize.length; i++) {
                        objects[i + 1] = deserialize[i];
                    }

                    if (MinecraftThreading.commonSide() == Side.CLIENT) {
                        objects[0] = Minecraft.getMinecraft().player;
                    } else {
                        objects[0] = ctx.getServerHandler().player;
                    }
                    MinecraftThreading.submit(() -> {
                        try {
                            cached.getMethodAccessor().invoke(cached.isStatic() ? null : InstanceHolder.getInstance(cached.getOwner()), objects);
                        } catch (Exception e) {
                            PanguCore.getLogger().error("Unable to handle @Remote for " + cached.messageClass.toGenericString(), e);
                        }
                    }, cached.nativeMessage.sync);
                } else
                    MinecraftThreading.submit(() -> {
                        try {
                            cached.getMethodAccessor().invoke(cached.isStatic() ? null : InstanceHolder.getInstance(cached.getOwner()), ByteSerialization.INSTANCE.deserialize(message.getBytes(), cached.methodArgs));
                        } catch (Exception e) {
                            PanguCore.getLogger().error("Unable to handle @Remote for " + cached.messageClass.toGenericString(), e);
                        }
                    }, cached.nativeMessage.sync);
            } catch (Exception e) {
                PanguCore.getLogger().error("Unable to handle @Remote for " + cached.messageClass.toGenericString(), e);
            }
            return null;
        }
    }

    public static class CachedRemoteMessage {
        private int id;
        private Class messageClass;
        private MethodAccessor methodAccessor;
        private Class owner;
        private Class[] methodArgs;
        private RemoteTransformer.RemoteMessage nativeMessage;
        private boolean withEntityPlayer, isStatic;
        private SimpleNetworkWrapper network;

        public CachedRemoteMessage(RemoteTransformer.RemoteMessage remoteMessage) throws Exception {
            this.id = remoteMessage.id;
            this.messageClass = PanguClassLoader.getInstance().defineClass(remoteMessage.messageClassName, remoteMessage.messageClassBytes);
            this.methodArgs = ReflectUtils.fromTypes(remoteMessage.methodArgs);
            this.network = SimpleMessageRegister.getNetworkWrapper(messageClass);
            this.withEntityPlayer = methodArgs.length > 0 && EntityPlayer.class.isAssignableFrom(methodArgs[0]);
            this.nativeMessage = remoteMessage;
        }

        public Class getOwner() {
            if (owner == null) {
                owner = ReflectUtils.forName(nativeMessage.ownerClass);
            }
            return owner;
        }

        public MethodAccessor getMethodAccessor() throws Exception {
            if (methodAccessor == null) {
                Method method = getOwner().getDeclaredMethod(nativeMessage.methodName, methodArgs);
                this.methodAccessor = FastReflection.create(method);
                this.isStatic = Modifier.isStatic(method.getModifiers());
            }
            return methodAccessor;
        }

        public boolean isStatic() throws Exception {
            if (methodAccessor == null) getMethodAccessor();
            return isStatic;
        }
    }
}
