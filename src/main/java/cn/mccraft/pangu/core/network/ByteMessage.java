package cn.mccraft.pangu.core.network;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ByteMessage implements IMessage {
    @NonNull
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
