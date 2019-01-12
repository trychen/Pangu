package cn.mccraft.pangu.core.network;

import io.netty.buffer.ByteBuf;
import lombok.*;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ByteMessage implements IMessage {
    private byte[] bytes;

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = buf.readByte();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(bytes.length);
        for (byte aByte : bytes) {
            buf.writeByte(aByte);
        }
    }
}
