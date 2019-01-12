package cn.mccraft.pangu.core.util.data.builtin;

import cn.mccraft.pangu.core.util.data.ByteSerializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum TextComponentSerializer implements ByteSerializer<ITextComponent> {
    INSTANCE;

    @Override
    public void serialize(DataOutputStream stream, ITextComponent object) throws IOException {
        stream.writeUTF(ITextComponent.Serializer.componentToJson(object));
    }

    @Override
    public ITextComponent deserialize(DataInputStream in) throws IOException {
        return ITextComponent.Serializer.jsonToComponent(in.readUTF());
    }
}
