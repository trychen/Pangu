package cn.mccraft.pangu.core.client.tooltip;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ToolTip implements IMessage {
    private String text;
    private ToolTipType type;

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        text = ByteBufUtils.readUTF8String(byteBuf);
        type = ToolTipType.valueOf(byteBuf.readByte());
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, text);
        byteBuf.writeByte(type.getId());
    }
}
