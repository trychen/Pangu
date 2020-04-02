package cn.mccraft.pangu.core.util.data.builtin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.trychen.bytedatastream.ByteDeserializer;
import com.trychen.bytedatastream.ByteSerializer;
import com.trychen.bytedatastream.DataInput;
import com.trychen.bytedatastream.DataOutput;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public enum NBTSerializer implements ByteSerializer<NBTTagCompound>, ByteDeserializer<NBTTagCompound>, JsonSerializer<NBTTagCompound>, JsonDeserializer<NBTTagCompound> {
    INSTANCE;

    @Override
    public void serialize(DataOutput stream, @Nonnull NBTTagCompound nbt) throws IOException {
        try {
            CompressedStreamTools.write(nbt, stream);
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }

    @Override
    public NBTTagCompound deserialize(DataInput in) throws IOException {
        try {
            return CompressedStreamTools.read(in, new NBTSizeTracker(2097152L));
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }

    @Override
    public NBTTagCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        byte[] raw = context.deserialize(json, new TypeToken<byte[]>(){}.getType());
        ByteArrayInputStream stream = new ByteArrayInputStream(raw);
        try {
            return CompressedStreamTools.read(new DataInputStream(stream), new NBTSizeTracker(2097152L));
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }

    @Override
    public JsonElement serialize(NBTTagCompound src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            CompressedStreamTools.write(src, new DataOutputStream(stream));
            return context.serialize(stream.toByteArray());
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }
}
