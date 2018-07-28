package cn.mccraft.pangu.core.client.tooltip;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ToolTip implements IMessage {
    private String text;
    private int duration;
    private ToolTipStyle style;

    public ToolTip() {
    }

    public ToolTip(String text) {
        this(text, ToolTipStyle.NORMAL);
    }

    public ToolTip(String text, ToolTipStyle type) {
        this(text, 60, type);
    }
    public ToolTip(String text, int duration) {
        this(text, duration, ToolTipStyle.NORMAL);
    }

    public ToolTip(String text, int duration, ToolTipStyle style) {
        this.text = text;
        this.duration = duration;
        this.style = style;
    }

    public String getText() {
        return text;
    }

    public ToolTipStyle getStyle() {
        return style;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        text = ByteBufUtils.readUTF8String(byteBuf);
        duration = byteBuf.readInt();
        style = ToolTipStyle.valueOf(byteBuf.readByte());
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, text);
        byteBuf.writeInt(duration);
        byteBuf.writeByte(style.getId());
    }
}
