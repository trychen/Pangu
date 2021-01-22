package cn.mccraft.pangu.core.network.bridge;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@AllArgsConstructor
@Data
public
class Packet implements IMessage {
    protected String key;
    protected byte[] bytes;

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] utf8Bytes = new byte[ByteBufUtils.readVarInt(buf, 4)];
        buf.readBytes(utf8Bytes);
        this.key = new String(utf8Bytes, StandardCharsets.UTF_8);

        this.bytes = new byte[ByteBufUtils.readVarInt(buf, 4)];
        buf.readBytes(bytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] utf8Bytes = key.getBytes(StandardCharsets.UTF_8);
        ByteBufUtils.writeVarInt(buf, utf8Bytes.length, 4);
        buf.writeBytes(utf8Bytes);

        ByteBufUtils.writeVarInt(buf, bytes.length, 4);
        buf.writeBytes(bytes);
    }
}
