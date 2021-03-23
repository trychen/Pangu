package cn.mccraft.pangu.core.network.bridge;

import cn.mccraft.pangu.core.util.GZIPUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompressMultiPartPacketBuffer extends MultiPartPacketBuffer{
    public CompressMultiPartPacketBuffer(String key, byte[] raw) {
        byte[] bytes = GZIPUtils.compress(raw);
        short total = (short) ((bytes.length / 30000) + (bytes.length % 30000 > 0 ? 1 : 0));

        UUID id = UUID.randomUUID();
        packets = new MultiPartPacket[total];

        for (short i = 0; i < total; i++) {
            MultiPartPacket packet = new MultiPartPacket();

            packet.setUuid(id);
            packet.setCurrent(i);
            packet.setTotal(total);
            packet.setKey(key);
            packet.setBytes(
                    ArrayUtils.subarray(bytes, i * 30000, Math.min(bytes.length, (i + 1) * 30000))
            );

            packets[i] = packet;
        }
    }

    public CompressMultiPartPacketBuffer(MultiPartPacket packet) {
        packets = new MultiPartPacket[packet.getTotal()];
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[size];
        int cursor = 0;
        for (MultiPartPacket packet : packets) {
            System.arraycopy(packet.getBytes(), 0, bytes, cursor, packet.getBytes().length);
            cursor += packet.getBytes().length;
        }

        return GZIPUtils.uncompress(bytes);
    }
}
