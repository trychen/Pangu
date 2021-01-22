package cn.mccraft.pangu.core.network.bridge;

import cn.mccraft.pangu.core.util.GZIPUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.nio.charset.StandardCharsets;

@EqualsAndHashCode(callSuper = true)
@Data
public
class CompressPacket extends Packet {
    public CompressPacket() {
    }

    public CompressPacket(String key, byte[] bytes) {
        super(key, bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] utf8Bytes = new byte[ByteBufUtils.readVarInt(buf, 4)];
        buf.readBytes(utf8Bytes);
        this.key = new String(utf8Bytes, StandardCharsets.UTF_8);

        byte[] compressBytes = new byte[ByteBufUtils.readVarInt(buf, 4)];
        buf.readBytes(compressBytes);

        this.bytes = GZIPUtils.uncompress(compressBytes);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] utf8Bytes = key.getBytes(StandardCharsets.UTF_8);
        ByteBufUtils.writeVarInt(buf, utf8Bytes.length, 4);
        buf.writeBytes(utf8Bytes);

        byte[] compressBytes = GZIPUtils.compress(this.bytes);
        ByteBufUtils.writeVarInt(buf, compressBytes.length, 4);
        buf.writeBytes(compressBytes);
    }
}
