package cn.mccraft.pangu.core.util.data.builtin;

import com.trychen.bytedatastream.ByteDeserializer;
import com.trychen.bytedatastream.ByteSerializer;
import com.trychen.bytedatastream.DataInput;
import com.trychen.bytedatastream.DataOutput;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public enum ResourceLocationSerializer implements ByteSerializer<ResourceLocation>, ByteDeserializer<ResourceLocation> {
    INSTANCE;
    @Override
    public ResourceLocation deserialize(DataInput in) throws IOException {
        return new ResourceLocation(in.readUTF(), in.readUTF());
    }

    @Override
    public void serialize(DataOutput out, ResourceLocation object) throws IOException {
        out.writeUTF(object.getNamespace());
        out.writeUTF(object.getPath());
    }
}
