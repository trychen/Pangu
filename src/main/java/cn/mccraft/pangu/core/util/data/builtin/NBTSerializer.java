package cn.mccraft.pangu.core.util.data.builtin;

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
import java.io.IOException;

public enum NBTSerializer implements ByteSerializer<NBTTagCompound>, ByteDeserializer<NBTTagCompound> {
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
}
