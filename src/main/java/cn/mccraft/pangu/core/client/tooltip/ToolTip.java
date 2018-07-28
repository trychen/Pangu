package cn.mccraft.pangu.core.client.tooltip;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ToolTip implements IMessage {
    private String text;
    private int duration;
    private ToolTipStyle style;
    private boolean animated;

    public ToolTip() {
    }

    public ToolTip(String text) {
        this(text, ToolTipStyle.NORMAL);
    }

    public ToolTip(String text, ToolTipStyle type) {
        this(text, 200, type);
    }
    public ToolTip(String text, int duration) {
        this(text, duration, ToolTipStyle.NORMAL);
    }

    public ToolTip(String text, int duration, ToolTipStyle style) {
        this(text, duration, style, false);
    }

    public ToolTip(String text, int duration, ToolTipStyle style, boolean animated) {
        this.text = text;
        this.duration = duration;
        this.style = style;
        this.animated = animated;
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

    public boolean isAnimated() {
        return animated;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStyle(ToolTipStyle style) {
        this.style = style;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
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
