package cn.mccraft.pangu.core.network;

import cn.mccraft.pangu.core.loader.Load;
import com.trychen.bytedatastream.ByteSerialization;
import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.NonNull;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;

@Data
@Deprecated
public class KeyBasedMessage extends ByteMessage {

    @NonNull
    private String key;

    public KeyBasedMessage(@NonNull String key, byte[] bytes) {
        super(bytes);
        this.key = key;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        key = ByteBufUtils.readUTF8String(buf);
        super.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            buf.writeBytes(ByteSerialization.serialize(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.toBytes(buf);
    }
}
