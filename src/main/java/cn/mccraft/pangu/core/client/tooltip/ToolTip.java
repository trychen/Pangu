package cn.mccraft.pangu.core.client.tooltip;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ToolTip implements IMessage {
    /**
     * The text to display in the tooltip
     */
    private String text;

    /**
     * The duration displaying the tooltip
     */
    private int duration;

    /**
     * The style of the tooltip
     */
    private ToolTipStyle style;

    /**
     * Whether using animation while displaying and concealing
     */
    private boolean animated;

    /**
     * No-parameter construction for message channel
     */
    public ToolTip() {
    }

    /**
     * Normal style tooltip with the duration of 200ms
     */
    public ToolTip(String text) {
        this(text, ToolTipStyle.NORMAL);
    }

    /**
     * Custom style tooltip with the duration of 200ms
     */
    public ToolTip(String text, ToolTipStyle type) {
        this(text, 200, type);
    }
    public ToolTip(String text, int duration) {
        this(text, duration, ToolTipStyle.NORMAL);
    }
    public ToolTip(String text, int duration, ToolTipStyle style) {
        this(text, duration, style, true);
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
        style = ToolTipStyle.valueOf(ByteBufUtils.readUTF8String(byteBuf));
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, text);
        byteBuf.writeInt(duration);
        ByteBufUtils.writeUTF8String(byteBuf, style.getName());
    }
}
