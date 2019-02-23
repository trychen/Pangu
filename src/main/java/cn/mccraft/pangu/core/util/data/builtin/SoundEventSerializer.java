package cn.mccraft.pangu.core.util.data.builtin;

import com.trychen.bytedatastream.ByteDeserializer;
import com.trychen.bytedatastream.ByteSerializer;
import com.trychen.bytedatastream.DataInput;
import com.trychen.bytedatastream.DataOutput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.io.IOException;

public enum SoundEventSerializer implements ByteSerializer<SoundEvent>, ByteDeserializer<SoundEvent> {
    INSTANCE;
    @Override
    public SoundEvent deserialize(DataInput in) throws IOException {
        return SoundEvent.REGISTRY.getObject(in.read(ResourceLocation.class));
    }

    @Override
    public void serialize(DataOutput out, SoundEvent object) throws IOException {
        out.write(object.getSoundName());
    }
}
