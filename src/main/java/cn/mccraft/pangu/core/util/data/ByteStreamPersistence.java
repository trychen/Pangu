package cn.mccraft.pangu.core.util.data;

import cn.mccraft.pangu.core.loader.AutoWired;
import com.trychen.bytedatastream.ByteSerialization;

import java.io.IOException;
import java.lang.reflect.Type;

@AutoWired
public enum ByteStreamPersistence implements Persistence {
    INSTANCE;

    @Override
    public byte[] serialize(Object[] object, Type[] types) throws IOException {
        return ByteSerialization.serialize(object, types);
    }

    @Override
    public Object[] deserialize(byte[] bytes, Type[] types) throws IOException {
        return ByteSerialization.deserialize(bytes, types);
    }
}
