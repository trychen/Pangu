package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.util.data.ByteSerialization;
import lombok.Data;
import lombok.NonNull;
import lombok.val;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Data
public class ToolTip {
    /**
     * Register byte serializer
     */
    static {
        ByteSerialization.INSTANCE.register(ToolTip.class, (out, toolTip) -> {
            out.writeUTF(toolTip.getText());
            out.writeInt(toolTip.getDuration());
            out.writeUTF(toolTip.getStyle().getName());
            out.writeBoolean(toolTip.isAnimated());
        }, in -> {
            val tooltip = new ToolTip(in.readUTF());
            tooltip.setDuration(in.readInt());
            tooltip.setStyle(ToolTipStyle.valueOf(in.readUTF()));
            tooltip.setAnimated(in.readBoolean());
            return tooltip;
        });
    }

    /**
     * The text to display in the tooltip
     */
    @NonNull
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

    public void display(EntityPlayer entityPlayer) {
        ToolTipRenderer.INSTANCE.set(entityPlayer, this);
    }
}
