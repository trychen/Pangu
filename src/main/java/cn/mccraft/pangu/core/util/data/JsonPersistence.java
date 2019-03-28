package cn.mccraft.pangu.core.util.data;

import java.io.IOException;
import java.lang.reflect.Type;

public enum JsonPersistence implements Persistence {
    INSTANCE;

    @Override
    public byte[] serialize(Object[] object, Type[] types) throws IOException {
        return new byte[0];
    }

    @Override
    public Object[] deserialize(byte[] bytes, Type[] types) throws IOException {
        return new Object[0];
    }
}
