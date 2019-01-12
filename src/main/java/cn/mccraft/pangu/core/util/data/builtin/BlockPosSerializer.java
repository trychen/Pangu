package cn.mccraft.pangu.core.util.data.builtin;

import cn.mccraft.pangu.core.util.data.ByteSerializer;
import io.netty.handler.codec.EncoderException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum BlockPosSerializer implements ByteSerializer<BlockPos> {
    INSTANCE;

    @Override
    public void serialize(DataOutputStream stream, BlockPos pos) throws IOException {
        stream.writeLong(pos.toLong());
    }

    @Override
    public BlockPos deserialize(DataInputStream in) throws IOException {
        return BlockPos.fromLong(in.readLong());
    }
}
