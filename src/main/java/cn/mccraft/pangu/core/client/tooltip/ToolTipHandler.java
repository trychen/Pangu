package cn.mccraft.pangu.core.client.tooltip;

import cn.mccraft.pangu.core.network.Network;
import cn.mccraft.pangu.core.network.RegSimpleMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

@RegSimpleMessage(id = Network.TOOLTIP_MESSAGE_ID, message = ToolTip.class, side = Side.CLIENT)
public class ToolTipHandler implements IMessageHandler<ToolTip, ToolTip> {
    @Override
    public ToolTip onMessage(ToolTip toolTip, MessageContext messageContext) {
        ToolTipRenderer.INSTANCE.set(toolTip);
        return null;
    }
}
