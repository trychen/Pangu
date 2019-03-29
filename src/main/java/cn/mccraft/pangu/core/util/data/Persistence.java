package cn.mccraft.pangu.core.util.data;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

public interface Persistence {
    byte[] serialize(String[] parameterNames, Object[] objects, Type[] types) throws IOException;
    Object[] deserialize(String[] parameterNames, byte[] bytes, Type[] types) throws IOException;

    default byte[] serialize(String[] parameterNames, Object... objects) throws IOException {
        return serialize(parameterNames, objects, Arrays.stream(objects).map(Object::getClass).toArray(Class[]::new));
    }
}
