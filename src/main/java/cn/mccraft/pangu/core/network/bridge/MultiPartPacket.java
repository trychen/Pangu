package cn.mccraft.pangu.core.network.bridge;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public
class MultiPartPacket implements IMessage {
    /**
     * Unique id for every packet
     */
    protected UUID uuid;

    /**
     * Total packet size
     */
    protected short total;

    /**
     * Current packet index
     */
    protected short current;

    protected String key;
    protected byte[] bytes;

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = new UUID(buf.readLong(), buf.readLong());

        this.total = buf.readShort();
        this.current = buf.readShort();

        byte[] utf8Bytes = new byte[ByteBufUtils.readVarInt(buf, 4)];
        buf.readBytes(utf8Bytes);
        this.key = new String(utf8Bytes, StandardCharsets.UTF_8);

        this.bytes = new byte[ByteBufUtils.readVarInt(buf, 4)];
        buf.readBytes(bytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());

        buf.writeShort(total);
        buf.writeShort(current);

        byte[] utf8Bytes = key.getBytes(StandardCharsets.UTF_8);
        ByteBufUtils.writeVarInt(buf, utf8Bytes.length, 4);
        buf.writeBytes(utf8Bytes);

        ByteBufUtils.writeVarInt(buf, bytes.length, 4);
        buf.writeBytes(bytes);
    }
}
