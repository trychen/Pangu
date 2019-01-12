package cn.mccraft.pangu.core.util.data;


import cn.mccraft.pangu.core.util.data.builtin.BlockPosSerializer;
import cn.mccraft.pangu.core.util.data.builtin.ItemStackSerializer;
import cn.mccraft.pangu.core.util.data.builtin.NBTSerializer;
import cn.mccraft.pangu.core.util.data.builtin.TextComponentSerializer;
import lombok.Getter;
import lombok.val;
import lombok.var;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public enum ByteSerialization {
    INSTANCE;

    @Getter
    private Map<Type, ByteSerializer> serializer = new HashMap<>();

    ByteSerialization() {
        register(byte[].class, new SimpleByteSerializer<>((out, o) -> {
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

        register(int.class, (out, o) -> out.writeInt(o), in -> in.readInt());
        register(float.class, (out, o) -> out.writeFloat(o), in -> in.readFloat());
        register(double.class, (out, o) -> out.writeDouble(o), in -> in.readDouble());
        register(boolean.class, (out, o) -> out.writeBoolean(o), in -> in.readBoolean());
        register(short.class, (out, o) -> out.writeShort(o), in -> in.readShort());
        register(byte.class, (out, o) -> out.writeByte(o), in -> in.readByte());
        register(String.class, (out, o) -> out.writeUTF(o), in -> in.readUTF());
        register(ItemStack.class, ItemStackSerializer.INSTANCE);
        register(NBTTagCompound.class, NBTSerializer.INSTANCE);
        register(BlockPos.class, BlockPosSerializer.INSTANCE);
        register(ITextComponent.class, TextComponentSerializer.INSTANCE);
    }

    public void serialize(DataOutputStream out, Object object) throws IOException {
        serialize(out, object, object.getClass());
    }

    public void serialize(DataOutputStream out, Object object, Type type) throws IOException {
        var byteSerializable = serializer.get(type);
        if (byteSerializable == null && object instanceof ByteSerializer) {
            serializer.put(byteSerializable.getClass(), byteSerializable = (ByteSerializer) object);
        }
        if (byteSerializable == null) new RuntimeException("Couldn't find any serialization");

        byteSerializable.serialize(out, object);
    }

    /**
     * 序列化参数
     * @param object
     * @return
     * @throws IOException
     */
    public byte[] serialize(Object[] object) throws IOException {
        val stream = new ByteArrayOutputStream();
        val out = new DataOutputStream(stream);
        for (Object o : object) {
            serialize(out, o);
        }
        return stream.toByteArray();
    }

    /**
     * 序列化参数
     * @param object
     * @param types 类型
     * @return
     * @throws IOException
     */
    public byte[] serialize(Object[] object, Type[] types) throws IOException {
        if (object.length != types.length) throw new IllegalArgumentException("objects.length != types.length");
        val stream = new ByteArrayOutputStream();
        val out = new DataOutputStream(stream);
        for (int i = 0; i < object.length; i++) {
            serialize(out, object[i], types[i]);
        }
        return stream.toByteArray();
    }

    public Object deserialize(DataInputStream in, Type type) throws IOException {
        val byteSerializable = serializer.get(type);
        if (byteSerializable == null) return null;
        return byteSerializable.deserialize(in);
    }

    public Object[] deserialize(byte[] data, Type[] types) throws IOException {
        val stream = new ByteArrayInputStream(data);
        val in = new DataInputStream(stream);
        val objects = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            ByteSerializer byteSerializable = serializer.get(types[i]);
            if (byteSerializable == null) throw new RuntimeException("Couldn't find any serialization for type " + types[i].getTypeName());
            objects[i] = deserialize(in, types[i]);
        }
        return objects;
    }

    public <T> void register(Class<T> tClass, ByteSerializer<T> serializable) {
        serializer.put(tClass, serializable);
    }

    public <T> void register(Class<T> tClass, SimpleByteSerializer.Serializer<T> serializer, SimpleByteSerializer.Deserializer<T> deserializer) {
        register(tClass, new SimpleByteSerializer<>(serializer, deserializer));
    }
}
