package cn.mccraft.pangu.core.network.bridge;

import cn.mccraft.pangu.core.PanguCore;
import cn.mccraft.pangu.core.network.BridgeHandler;
import cn.mccraft.pangu.core.util.GZIPUtils;
import cn.mccraft.pangu.core.util.Games;
import cn.mccraft.pangu.core.util.Threads;
import lombok.AllArgsConstructor;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public
enum CompressMultiPartPacketHandler implements IMessageHandler<MultiPartPacket, IMessage> {
    INSTANCE;

    private static final Map<UUID, CompressMultiPartPacketBuffer> MULTI_PART_PACKET_BUFFER = new ConcurrentHashMap<>();

    @Override
    public IMessage onMessage(MultiPartPacket message, MessageContext ctx) {
        BridgeHandler.Solution solution = BridgeHandler.SOLUTIONS.get(message.getKey());

        // 空检测
        if (solution == null) {
            PanguCore.getLogger().error("Couldn't find any solution to handle @Bridge message: " + message.getKey());
            return null;
        }

        CompressMultiPartPacketBuffer buffer = MULTI_PART_PACKET_BUFFER.computeIfAbsent(message.getUuid(), it -> new CompressMultiPartPacketBuffer(message));
        buffer.process(message);

        if (!buffer.isComplete()) return null;

        MULTI_PART_PACKET_BUFFER.remove(message.getUuid());

        if (!solution.isSync()) {
            try {
                solution.solve(solution.side().isServer() ? ctx.getServerHandler().player : Games.player(), buffer.getBytes());
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
                solution.solve(solution.side().isServer() ? ctx.getServerHandler().player : Games.player(), buffer.getBytes());
            } catch (Throwable e) {
                PanguCore.getLogger().error("Unable to handle @Bridge for " + message.getKey(), e);
            }
        });
        return null;
    }
}
