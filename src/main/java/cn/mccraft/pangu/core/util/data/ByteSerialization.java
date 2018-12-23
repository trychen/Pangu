package cn.mccraft.pangu.core.util.data;


import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public enum ByteSerialization {
    INSTANCE;
    private Map<Type, ByteSerializable> serializer = new HashMap<>();

    ByteSerialization() {
        register(byte[].class, new SimpleByteSerializable<>((out, o) -> {
            out.writeInt(o.length);
            out.write(o);
        }, in -> {
            int length = in.readInt();
            byte[] bytes = new byte[length];
            for (int i = 0; i < length; i++) {
                bytes[i] = in.readByte();
            }
            return bytes;
        }));

        register(String.class, new SimpleByteSerializable<>((out, o) -> out.writeUTF(o), in -> in.readUTF()));
        register(int.class, new SimpleByteSerializable<>((out, o) -> out.writeInt(o), in -> in.readInt()));
        register(float.class, new SimpleByteSerializable<>((out, o) -> out.writeFloat(o), in -> in.readFloat()));
        register(double.class, new SimpleByteSerializable<>((out, o) -> out.writeDouble(o), in -> in.readDouble()));
        register(boolean.class, new SimpleByteSerializable<>((out, o) -> out.writeBoolean(o), in -> in.readBoolean()));
        register(short.class, new SimpleByteSerializable<>((out, o) -> out.writeShort(o), in -> in.readShort()));
        register(byte.class, new SimpleByteSerializable<>((out, o) -> out.writeByte(o), in -> in.readByte()));
    }

    public void serialize(DataOutputStream out, Object object) throws IOException {
        ByteSerializable byteSerializable = serializer.get(object.getClass());
        if (byteSerializable == null && object instanceof ByteSerializable) {
            serializer.put(byteSerializable.getClass(), byteSerializable = (ByteSerializable) object);
        }
        if (byteSerializable == null) new RuntimeException("Couldn't find any serialization");

        byteSerializable.serialize(out, object);
    }
    public byte[] serialize(Object[] object) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        for (Object o : object) {
            serialize(out, o);
        }
        return stream.toByteArray();
    }

    public Object deserialize(DataInputStream in, Type type) throws IOException {
        ByteSerializable byteSerializable = serializer.get(type);
        if (byteSerializable == null) return null;
        return byteSerializable.deserialize(in);
    }

    public Object[] deserialize(byte[] data, Type[] types) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        DataInputStream in = new DataInputStream(stream);
        Object[] objects = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            ByteSerializable byteSerializable = serializer.get(types[i]);
            if (byteSerializable == null) throw new RuntimeException("Couldn't find any serialization for type " + types[i].getTypeName());
            objects[i] = deserialize(in, types[i]);
        }
        return objects;
    }

    public <T> void register(Class<T> tClass, ByteSerializable<T> serializable) {
        serializer.put(tClass, serializable);
    }

    public Map<Type, ByteSerializable> getSerializer() {
        return serializer;
    }
}
