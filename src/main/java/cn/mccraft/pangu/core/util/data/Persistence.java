package cn.mccraft.pangu.core.util.data;

import com.trychen.bytedatastream.DataOutput;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

public interface Persistence {
    byte[] serialize(Object[] object, Type[] types) throws IOException;
    Object[] deserialize(byte[] bytes, Type[] types) throws IOException;

    default byte[] serialize(Object... objects) throws IOException {
        return serialize(objects, (Class[]) Arrays.stream(objects).map(Object::getClass).toArray());
    }
}
