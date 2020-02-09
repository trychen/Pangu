package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.network.Bridge;
import com.trychen.bytedatastream.ByteSerialization;
import lombok.Data;
import lombok.NonNull;
import lombok.val;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@Data
public class ToolTip {
    /**
     * Register byte serializer
     */
    static {
        ByteSerialization.register(ToolTip.class, (out, toolTip) -> {
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
        ToolTip.set(entityPlayer, this);
    }

    /**
     * Display ToolTip, will replace the previous one if exists.
     * ToolTip text width (from {@code FontRenderer.getStringWidth(String text)})
     * cannot be longer than 387. If text width is longer than 387, the beyond part
     * will be cut.
     */
    @Bridge(value = "ToolTips", side = Side.CLIENT)
    public static void set(@Nullable EntityPlayer entityPlayer, @Nonnull ToolTip toolTip) {
        ToolTipRenderer.INSTANCE.display(toolTip);
    }
}
