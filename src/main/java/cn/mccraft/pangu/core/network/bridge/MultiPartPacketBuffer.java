package cn.mccraft.pangu.core.network.bridge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public
class MultiPartPacketBuffer {
    protected MultiPartPacket[] packets;
    protected int size;

    public MultiPartPacketBuffer(String key, byte[] bytes) {
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

    public MultiPartPacketBuffer(MultiPartPacket packet) {
        packets = new MultiPartPacket[packet.getTotal()];
    }

    public void process(MultiPartPacket packet) {
        packets[packet.getCurrent()] = packet;
        size += packet.getBytes().length;
    }

    public boolean isComplete() {
        for (MultiPartPacket packet : packets) {
            if (packet == null) return false;
        }
        return true;
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[size];
        int cursor = 0;
        for (MultiPartPacket packet : packets) {
            System.arraycopy(packet.getBytes(), 0, bytes, cursor, packet.getBytes().length);
            cursor += packet.getBytes().length;
        }
        return bytes;
    }
}
