package cn.mccraft.pangu.core.util.data.builtin;

import com.trychen.bytedatastream.*;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

public enum TextComponentSerializer implements ByteSerializer<ITextComponent>, ByteDeserializer<ITextComponent> {
    INSTANCE;

    @Override
    public ITextComponent deserialize(DataInput in) throws IOException {
        return ITextComponent.Serializer.jsonToComponent(in.readUTF());
    }

    @Override
    public void serialize(DataOutput out, ITextComponent object) throws IOException {
        out.writeUTF(ITextComponent.Serializer.componentToJson(object));
    }
}
