package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.PanguCore;
import io.netty.buffer.ByteBuf;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToolTip implements IMessage {
    /**
     * The text to display in the tooltip
     */
    private String text;

    /**
     * The duration displaying the tooltip
     */
    private int duration = 200;

    /**
     * The style of the tooltip
     */
    private ToolTipStyle style = ToolTipStyle.NORMAL;

    /**
     * Whether using animation while displaying and concealing
     */
    private boolean animated = true;

    /**
     * No-parameter construction for message channel
     */
    public ToolTip() {
    }

    /**
     * Normal style tooltip with the duration of 200ms
     */
    public ToolTip(String text) {
        this();
        this.setText(text);
    }

    /**
     * Custom style tooltip with the duration of 200ms
     */
    @Deprecated
    public ToolTip(String text, ToolTipStyle type) {
        this(text, 200, type);
    }
    @Deprecated
    public ToolTip(String text, int duration) {
        this(text, duration, ToolTipStyle.NORMAL);
    }

    @Deprecated
    public ToolTip(String text, int duration, ToolTipStyle style) {
        this(text, duration, style, true);
    }

    @Deprecated
    public ToolTip(String text, int duration, ToolTipStyle style, boolean animated) {
        this(text);
        this.setDuration(duration);
        this.setStyle(style);
        this.setAnimated(animated);
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

    public ToolTip setText(String text) {
        this.text = text;
        return this;
    }

    public ToolTip setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ToolTip setStyle(ToolTipStyle style) {
        this.style = style;
        return this;
    }

    public ToolTip setAnimated(boolean animated) {
        this.animated = animated;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        text = ByteBufUtils.readUTF8String(byteBuf);
        duration = byteBuf.readInt();
        style = ToolTipStyle.valueOf(ByteBufUtils.readUTF8String(byteBuf));
        animated = byteBuf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, Objects.requireNonNull(text));
        byteBuf.writeInt(duration);
        ByteBufUtils.writeUTF8String(byteBuf, Objects.requireNonNull(style).getName());
        byteBuf.writeBoolean(animated);
    }

    /**
     * Send this ToolTips to player
     * @param entityPlayer the player to sent
     * @since 1.2.1.1
     */
    public void send(EntityPlayerMP entityPlayer) {
        PanguCore.getNetwork().sendTo(this, entityPlayer);
    }
}
