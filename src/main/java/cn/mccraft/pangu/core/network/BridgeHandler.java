package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.loader.AnnotationInjector;
import cn.mccraft.pangu.core.loader.AnnotationStream;
import cn.mccraft.pangu.core.loader.Load;
import cn.mccraft.pangu.core.network.bridge.*;
import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.Threads;
import lombok.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface BridgeHandler {
    Map<String, Solution> SOLUTIONS = new HashMap<>();

    static boolean send(String key, Object[] objects) {
        Solution solution = SOLUTIONS.get(key);

        // 空检测
        if (solution == null) {
            PanguCore.getLogger().error("Couldn't find any solution to send @Bridge message: " + key);
            return true;
        }

        // 检测是否能运行
        if (solution.isContinue()) return false;

        try {
            solution.solve(objects);
        } catch (Throwable e) {
            PanguCore.getLogger().error("Error while sending @Bridge info", e);
        }

        return true;
    }

    @AnnotationInjector.StaticInvoke
    static void registerMessages(AnnotationStream<Bridge> stream) {
        PanguCore.getLogger().info("Start register @Bridge message");

        stream.methodStream().forEach(method -> {
            try {
                Bridge bridge = method.getAnnotation(Bridge.class);

                if (bridge.value().isEmpty())
                    throw new IllegalAccessException("method with @Bridge haven't been hook by asm: " + method.toGenericString());

                if (SOLUTIONS.containsKey(bridge.value())) {
                    throw new IllegalAccessException("@Bridge with " + bridge.value() + " has already exists, try to change a key for " + method.toGenericString());
                }

                Solution solution = new BaseSolution(bridge, method);

                SOLUTIONS.put(bridge.value(), solution);
                PanguCore.getLogger().debug("Registered @Bridge message with key " + bridge.value() + " from method " + method.toGenericString());
            } catch (Throwable e) {
                PanguCore.getLogger().error("Couldn't register message for " + method.toGenericString(), e);
            }
        });
    }

    @Load
    static void registerPacket() {
        PanguCore.getNetwork().registerMessage(PacketHandler.INSTANCE, Packet.class, Network.BRIDGE_SERVER_MESSAGE, Side.SERVER);
        PanguCore.getNetwork().registerMessage(PacketHandler.INSTANCE, Packet.class, Network.BRIDGE_CLIENT_MESSAGE, Side.CLIENT);

        PanguCore.getNetwork().registerMessage(MultiPartPacketHandler.INSTANCE, MultiPartPacket.class, Network.BRIDGE_SERVER_MULTIPART_MESSAGE, Side.SERVER);
        PanguCore.getNetwork().registerMessage(MultiPartPacketHandler.INSTANCE, MultiPartPacket.class, Network.BRIDGE_CLIENT_MULTIPART_MESSAGE, Side.CLIENT);

        PanguCore.getNetwork().registerMessage(CompressMultiPartPacketHandler.INSTANCE, CompressMultiPartPacket.class, Network.BRIDGE_SERVER_COMPRESS_MULTIPART_MESSAGE, Side.SERVER);
        PanguCore.getNetwork().registerMessage(CompressMultiPartPacketHandler.INSTANCE, CompressMultiPartPacket.class, Network.BRIDGE_CLIENT_COMPRESS_MULTIPART_MESSAGE, Side.CLIENT);
//
        PanguCore.getNetwork().registerMessage(PacketHandler.INSTANCE, CompressPacket.class, Network.BRIDGE_SERVER_COMPRESS_MESSAGE, Side.SERVER);
        PanguCore.getNetwork().registerMessage(PacketHandler.INSTANCE, CompressPacket.class, Network.BRIDGE_CLIENT_COMPRESS_MESSAGE, Side.CLIENT);
    }

    @AllArgsConstructor
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

            if (!solution.isSync()) {
                try {
                    solution.solve(solution.side().isServer() ? ctx.getServerHandler().player : Games.player(), message.getBytes());
                } catch (Throwable e) {
                    PanguCore.getLogger().error("Unable to handle @Bridge for " + message.getKey(), e);
                }
                return null;
            }

            IThreadListener side = Threads.side(solution.side());
            if (side == null) {
                PanguCore.getLogger().error("Not a valid side for @Bridge message " + message.getKey());
                return null;
            }
            side.addScheduledTask(() -> {
                try {
                    solution.solve(solution.side().isServer() ? ctx.getServerHandler().player : Games.player(), message.getBytes());
                } catch (Throwable e) {
                    PanguCore.getLogger().error("Unable to handle @Bridge for " + message.getKey(), e);
                }
            });
            return null;
        }
    }

    interface Solution {
        void solve(EntityPlayer player, byte[] data) throws Exception;

        void solve(Object[] data) throws IOException;

        /**
         * Directly run the method without sending anything to other side.
         */
        boolean isContinue();

        boolean isSync();

        Side side();
    }
}
