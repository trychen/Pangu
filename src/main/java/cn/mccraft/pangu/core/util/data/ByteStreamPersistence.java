package cn.mccraft.pangu.core.util.data;

import cn.mccraft.pangu.core.loader.AutoWired;
import com.trychen.bytedatastream.ByteSerialization;

import java.io.IOException;
import java.lang.reflect.Type;

@AutoWired
public enum ByteStreamPersistence implements Persistence {
    INSTANCE;

    @Override
    public byte[] serialize(String[] parameterNames, Object[] objects, Type[] types) throws IOException {
        return ByteSerialization.serialize(objects, types);
    }

    @Override
    public Object[] deserialize(String[] parameterNames, byte[] bytes, Type[] types) throws IOException {
        return ByteSerialization.deserialize(bytes, types);
    }
}
