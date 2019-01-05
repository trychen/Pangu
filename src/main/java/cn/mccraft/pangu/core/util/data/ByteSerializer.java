package cn.mccraft.pangu.core.util.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface ByteSerializer<T> {
    void serialize(DataOutputStream stream, T object) throws IOException;
    T deserialize(DataInputStream in) throws IOException;
}
