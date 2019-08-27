package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.InstanceHolder;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.Sides;
import cn.mccraft.pangu.core.util.Threads;
import cn.mccraft.pangu.core.util.data.Persistence;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.MethodAccessor;
import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface BridgeHandler {
    Map<String, Solution> SOLUTIONS = new HashMap<>();

    static boolean send(String key, Object[] objects) {        // 是否在正在运行内置服务器
        // 是否在正在运行内置服务器
        if (Sides.isClient() && Games.isIntegratedServer()) return false;

        Solution solution = SOLUTIONS.get(key);

        // 空检测
        if (solution == null) {
            PanguCore.getLogger().error("Couldn't find any solution to send @Bridge message: " + key);
            return true;
        }

        // 检测是否能运行
        if (solution.isContinue()) return false;

        Threads.submit(() -> {
            try {
                solution.solve(objects);
            } catch (Exception e) {
                PanguCore.getLogger().error("Error while sending @Bridge info", e);
            }
        }, solution.isSync());

        return true;
    }

    @AnnotationInjector.StaticInvoke
    static void registerMessages(AnnotationStream<Bridge> stream) {
        PanguCore.getLogger().info("Start register @Bridge message");
        stream.methodStream().forEach(method -> {
            try {
                Bridge bridge = method.getAnnotation(Bridge.class);

                if (bridge.value().isEmpty()) throw new IllegalAccessException("method with @Bridge haven't been hook by asm: " + method.toGenericString());

                Solution solution = new BaseSolution(bridge, method);

                SOLUTIONS.put(bridge.value(), solution);
                PanguCore.getLogger().debug("Registered @Bridge message with key " + bridge.value() + " from method " + method.toGenericString());
            } catch (Exception e) {
                PanguCore.getLogger().error("Couldn't register message for " + method.toGenericString(), e);
            }
        });
    }

    interface Solution {
        void solve(EntityPlayer player, byte[] data) throws Exception;

        void solve(Object[] data) throws IOException;

        /**
         * Directly run the method without sending anything to other side.
         */
        boolean isContinue();

        boolean isSync();
    }

    class BaseSolution implements Solution {
        @NonNull
        private Bridge bridge;
        @Getter(lazy = true)
        private final Persistence persistence = InstanceHolder.getOrNewInstance(bridge.persistence());
        @NonNull
        private Method method;
        @Getter(lazy = true)
        private final Object ownerInstance = InstanceHolder.getInstance(method.getDeclaringClass());
        @Getter
        private boolean withEntityPlayerParameter;
        @Getter
        private Type[] actualParameterTypes;
        @Getter
        private boolean isStatic;
        @Getter
        private MethodAccessor methodAccessor;
        @Getter(lazy = true)
        private final String[] actualParameterNames = Arrays.stream(method.getParameters()).map(Parameter::getName).toArray(String[]::new);
        @Getter
        private boolean persistenceByParameterOrder;

        private BaseSolution(@NonNull Bridge bridge, @NonNull Method method) throws Exception {
            this.bridge = bridge;
            this.method = method;
            this.isStatic = Modifier.isStatic(method.getModifiers());
            this.withEntityPlayerParameter = method.getParameterTypes().length > 0 && method.getParameterTypes()[0] == EntityPlayer.class;
            this.actualParameterTypes = this.withEntityPlayerParameter ? Arrays.copyOfRange(method.getGenericParameterTypes(), 1, method.getGenericParameterTypes().length) : method.getGenericParameterTypes();
            this.methodAccessor = FastReflection.create(method);
            this.persistenceByParameterOrder = bridge.persistenceByParameterOrder();
            if (method.getParameterCount() > 0 && method.getParameters()[0].isNamePresent()) {
                this.persistenceByParameterOrder = false;
            }
        }

        @Override
        public void solve(EntityPlayer player, byte[] data) throws Exception {
            Object[] objects = getPersistence().deserialize(getActualParameterNames(), data, actualParameterTypes);
            if (isWithEntityPlayerParameter()) objects = ArrayUtils.add(objects, 0, player);
            getMethodAccessor().invoke(getOwnerInstance(), objects);
        }

        @Override
        public void solve(Object[] objects) throws IOException {
            Object[] actualParameters;

            //处理玩家参数
            if (isWithEntityPlayerParameter())
                actualParameters = Arrays.copyOfRange(objects, 1, objects.length);
            else
                actualParameters = objects;

            // 序列化
            byte[] bytes = getPersistence().serialize(getActualParameterNames(), actualParameters, actualParameterTypes, persistenceByParameterOrder);
            Packet packet = new Packet(bridge.value(), bytes);
            // 发包
            if (bridge.side().isClient()){
                if (isWithEntityPlayerParameter())
                    PanguCore.getNetwork().sendTo(packet, (EntityPlayerMP) objects[0]);
                else
                    PanguCore.getNetwork().sendToAll(packet);
            } else {
                PanguCore.getNetwork().sendToServer(packet);
            }
        }

        @Override
        public boolean isContinue() {
            return bridge.side() == Sides.currentThreadSide();
        }

        @Override
        public boolean isSync() {
            return bridge.sync();
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    class Packet implements IMessage {
        private String key;
        private byte[] bytes;

        @Override
        public void fromBytes(ByteBuf buf) {

            byte[] utf8Bytes = new byte[ByteBufUtils.readVarInt(buf, 2)];
            for (int i = 0; i < utf8Bytes.length; i++) {
                utf8Bytes[i] = buf.readByte();
            }
            key = new String(utf8Bytes, StandardCharsets.UTF_8);

            this.bytes = new byte[ByteBufUtils.readVarInt(buf, 2)];
            for (int i = 0; i < bytes.length; i++) {
                this.bytes[i] = buf.readByte();
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            byte[] utf8Bytes = key.getBytes(StandardCharsets.UTF_8);
            ByteBufUtils.writeVarInt(buf, utf8Bytes.length, 2);
            buf.writeBytes(utf8Bytes);

            ByteBufUtils.writeVarInt(buf, bytes.length, 2);
            buf.writeBytes(bytes);
        }
    }

    @Load
    static void registerPacket() {
        PanguCore.getNetwork().registerMessage(PacketHandler.INSTANCE, Packet.class, Network.BRIDGE_SERVER_MESSAGE, Side.SERVER);
        PanguCore.getNetwork().registerMessage(PacketHandler.INSTANCE, Packet.class, Network.BRIDGE_CLIENT_MESSAGE, Side.CLIENT);
    }

    enum PacketHandler implements IMessageHandler<Packet, IMessage> {
        INSTANCE;
        @Override
        public IMessage onMessage(Packet message, MessageContext ctx) {
            Solution solution = SOLUTIONS.get(message.getKey());

            // 空检测
            if (solution == null) {
                PanguCore.getLogger().error("Couldn't find any solution to handle @Bridge message: " + message.getKey());
                return null;
            }

            Threads.submit(() -> {
                try {
                    solution.solve(Sides.commonSide().isServer()?ctx.getServerHandler().player:Games.player(), message.getBytes());
                } catch (Exception e) {
                    PanguCore.getLogger().error("Unable to handle @Bridge for " + message.getKey(), e);
                }
            }, !solution.isSync());

            return null;
        }
    }
}
