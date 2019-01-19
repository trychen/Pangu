package cn.mccraft.pangu.core.util.data.builtin;

import com.trychen.bytedatastream.*;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import java.io.IOException;

public enum NBTSerializer implements ByteSerializer<NBTTagCompound>, ByteDeserializer<NBTTagCompound> {
    INSTANCE;

    @Override
    public void serialize(DataOutput stream, NBTTagCompound nbt) throws IOException {
        if (nbt == null) {
            stream.writeByte(0);
        } else {
            stream.writeByte(1);
            try {
                CompressedStreamTools.write(nbt, stream);
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }

    @Override
    public NBTTagCompound deserialize(DataInput in) throws IOException {
        byte b0 = in.readByte();

        if (b0 == 0) {
            return null;
        } else {
            try {
                return CompressedStreamTools.read(in, new NBTSizeTracker(2097152L));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }
}
